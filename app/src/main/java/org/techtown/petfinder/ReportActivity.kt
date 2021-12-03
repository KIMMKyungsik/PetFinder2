package org.techtown.petfinder

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.sangcomz.fishbun.FishBun
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.techtown.GlideApp
import org.techtown.adapters.ImageViewPagerAdapter
import org.techtown.datas.Petdata
import org.techtown.extensions.showImagePicker
import org.techtown.petfinder.databinding.ActivityReportBinding
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class ReportActivity  : AppCompatActivity() {

    companion object {
    enum class PetListType(val value: Int) {
        LOST(0), FIND(1)
    }

    private const val ARGUMENT_PET_LIST_TYPE = "ARGUMENT_PET_LIST_TYPE"

    public fun startActivity(fragment: Fragment, type: PetListType) {
        val intent = Intent(fragment.context, ReportActivity::class.java).apply {
            putExtra(ARGUMENT_PET_LIST_TYPE, type.value)
        }

        fragment.startActivity(intent)
    }
}


private val MAXIMUM_IMAGE_COUNT = 5

private lateinit var type: PetListType
private lateinit var binding: ActivityReportBinding

private lateinit var auth: FirebaseAuth
private lateinit var db: FirebaseFirestore
private lateinit var storage: FirebaseStorage

private lateinit var adapter: ImageViewPagerAdapter
private var progressDialog: ProgressDialog? = null


override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val type = intent?.getIntExtra(ARGUMENT_PET_LIST_TYPE, 0) ?: savedInstanceState?.getInt(ARGUMENT_PET_LIST_TYPE) ?: 0
    this.type = if (type == 0) {
        PetListType.LOST
    } else {
        PetListType.FIND
    }

    binding = ActivityReportBinding.inflate(layoutInflater)
    setContentView(binding.root)

    auth = FirebaseAuth.getInstance()
    db = FirebaseFirestore.getInstance()
    storage = FirebaseStorage.getInstance()

    initUi()
}

override fun onSaveInstanceState(outState: Bundle) {
    outState.putInt(ARGUMENT_PET_LIST_TYPE, type.value)

    super.onSaveInstanceState(outState)
}

private fun initUi() {
    with(binding) {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        toolbar.setOnMenuItemClickListener {
            lifecycleScope.launchWhenCreated {
                post()
            }

            return@setOnMenuItemClickListener false
        }

        (typeTextInput.editText as? AutoCompleteTextView)?.run {
            val adapter = ArrayAdapter(
                this@ReportActivity,
                R.layout.item_spinner_default,
                resources.getStringArray(R.array.type)
            )

            setAdapter(adapter)

            if (type == PetListType.LOST) {
                setText(adapter.getItem(0) as String, false)
            } else {
                setText(adapter.getItem(1) as String, false)
            }

            setOnItemClickListener { adapterView, view, i, l ->
                type = if (i == 0) {
                    PetListType.LOST
                } else {
                    PetListType.FIND
                }

                updateTypeUi()
            }

            updateTypeUi()
        }

        adapter = ImageViewPagerAdapter()
        viewPager.adapter = adapter
        dotsIndicator.setViewPager2(viewPager)

        addButton.isVisible = true
        removeButton.isVisible = false

        addButton.setOnClickListener { _ ->
            showImagePicker()
        }

        removeButton.setOnClickListener { _ ->
            val position = binding.viewPager.currentItem

            adapter.dataSet.removeAt(position)
            adapter.notifyItemRemoved(position)

            if (adapter.itemCount == 0) {
                binding.removeButton.isVisible = false
            }

            binding.addButton.isVisible = true
            binding.dotsIndicator.refreshDots()
        }

        (dateTextInput.editText as? AutoCompleteTextView)?.run {
            val calendar = Calendar.getInstance()
            tag = Calendar.getInstance()
            setText(
                DateUtils.formatDateTime(
                    this@ReportActivity,
                    calendar.timeInMillis,
                    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_YEAR
                )
            )

            setOnClickListener { v ->
                val calendar = v.tag as? Calendar ?: return@setOnClickListener

                val dialog = DatePickerDialog(
                    this@ReportActivity,
                    { _, y, m, d ->
                        val calendar = Calendar.getInstance().apply {
                            set(Calendar.YEAR, y)
                            set(Calendar.MONTH, m)
                            set(Calendar.DAY_OF_MONTH, d)
                        }

                        val dateTextView = (binding.dateTextInput.editText as? AutoCompleteTextView) ?: return@DatePickerDialog
                        dateTextView.tag = calendar
                        dateTextView.setText(
                            DateUtils.formatDateTime(
                                this@ReportActivity,
                                calendar.timeInMillis,
                                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_YEAR
                            )
                        )

                        dateTextView.clearFocus()
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
                )

                dialog.show()
            }
        }

        (areaTextInput.editText as? AutoCompleteTextView)?.run {
            val adapter = ArrayAdapter(
                this@ReportActivity,
                R.layout.item_spinner_default,
                resources.getStringArray(R.array.area)
            )

            setAdapter(adapter)
            setText(adapter.getItem(0) as String, false)
        }
    }
}

private fun updateTypeUi() {
    with(binding) {
        if (type == PetListType.LOST) {
            dateTextInput.hint = "실종일"
            areaTextInput.hint = "실종지역"

        } else {
            dateTextInput.hint = "발견일"
            areaTextInput.hint = "발견지역"
        }
    }
}

private fun showImagePicker() {
    val maxCount = MAXIMUM_IMAGE_COUNT - adapter.itemCount
    if (maxCount == 0) return

    showImagePicker(maxCount)
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == FishBun.FISHBUN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
        val urlList = data?.getParcelableArrayListExtra<Uri>(FishBun.INTENT_PATH)?.map { it.toString() } ?: return

        val startIndex: Int = adapter.dataSet.size
        adapter.dataSet.addAll(urlList)

        adapter.notifyItemRangeInserted(startIndex, urlList.size)

        if (adapter.itemCount >= MAXIMUM_IMAGE_COUNT) {
            binding.addButton.visibility = View.GONE
        }

        binding.removeButton.visibility = View.VISIBLE

        binding.dotsIndicator.refreshDots()
    }
}

private suspend fun post() {
    if (!isFieldValidated()) return

    progressDialog?.dismiss()
    progressDialog = ProgressDialog.show(this, null, "업로드 중... 잠시만 기다려 주세요", true, false)

    val title = binding.titleTextInput.editText!!.text.toString()
    val race = binding.raceTextInput.editText!!.text.toString()
    val age = binding.ageTextInput.editText!!.text.toString()
    val sex = if (binding.maleButton.isChecked) {
        binding.maleButton.text
    } else {
        binding.femaleButton.text
    }

    val date = ((binding.dateTextInput.editText as AutoCompleteTextView).tag as Calendar).time
    val area = (binding.areaTextInput.editText as AutoCompleteTextView).text.toString()
    val location = binding.locationTextInput.editText!!.text.toString()
    val contact = binding.contactTextInput.editText!!.text.toString()
    val content = binding.contentTextInput.editText!!.text.toString()
    val images = adapter.dataSet


    val documentReference = db.collection("pet").document()


    val metadata = StorageMetadata.Builder().setContentType("image/jpeg").build()

    withContext(Dispatchers.IO) {
        var success = false
        val uploadTasks = ArrayList<UploadTask>()
        val storageReferences = ArrayList<StorageReference>()

        var time = System.currentTimeMillis()

        images.forEach {

            val storageReference = storage.reference.child("pet").child(documentReference.id).child("${time++}.jpg")

            try {

                val bitmap = GlideApp.with(this@ReportActivity)
                    .asBitmap()
                    .load(it)
                    .centerInside()
                    .override(1920, 1280)
                    .submit()
                    .get()

                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val bytes = outputStream.toByteArray()

                storageReferences.add(storageReference)


                uploadTasks.add(storageReference.putBytes(bytes, metadata))

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        try {

            Tasks.await(Tasks.whenAllComplete(uploadTasks))

            val pfPet = Petdata(
                writerUid = auth.uid!!,
                writerName = auth.currentUser!!.displayName ?: "-",
                writerContact = contact,


                pictures = storageReferences
                    .filterIndexed { index, _ -> uploadTasks.getOrNull(index)?.isSuccessful == true }
                    .map { it.toString() },
                title = title,
                race = race,
                age = age,
                sex = sex.toString(),
                date = date,
                area = area,
                location = location,
                content = content,

                type = type.value,
                activation = true
            )


            Tasks.await(documentReference.set(pfPet))

            success = true

        } catch (e: Exception) {
            e.printStackTrace()

            success = true

        } finally {
            withContext(Dispatchers.Main) {
                progressDialog?.dismiss()

                if (success) {
                    Toast.makeText(this@ReportActivity, "등록되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()

                } else {
                    Toast.makeText(this@ReportActivity, "오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

private fun isFieldValidated(): Boolean {
    var isValidation = true

    with(binding) {
        if (adapter.itemCount == 0) {
            imageErrorTextView.isVisible = true
            isValidation = false

        } else {
            imageErrorTextView.isVisible = false
        }

        if (titleTextInput.editText!!.text.toString().isBlank()) {
            titleTextInput.error = "제목을 입력해 주세요."
            isValidation = false

        } else {
            titleTextInput.error = null
            titleTextInput.isErrorEnabled = false
        }

        if (titleTextInput.editText!!.text.toString().isBlank()) {
            titleTextInput.error = "제목을 입력해 주세요."
            isValidation = false

        } else {
            titleTextInput.error = null
            titleTextInput.isErrorEnabled = false
        }

        if (raceTextInput.editText!!.text.toString().isBlank()) {
            raceTextInput.error = "품종을 입력해 주세요."
            isValidation = false

        } else {
            raceTextInput.error = null
            raceTextInput.isErrorEnabled = false
        }

        if (ageTextInput.editText!!.text.toString().isBlank()) {
            ageTextInput.error = "나이를 입력해 주세요."
            isValidation = false

        } else {
            ageTextInput.error = null
            ageTextInput.isErrorEnabled = false
        }

        if (locationTextInput.editText!!.text.toString().isBlank()) {
            locationTextInput.error = "상세한 위치를 입력해 주세요."
            isValidation = false

        } else {
            locationTextInput.error = null
            locationTextInput.isErrorEnabled = false
        }

        if (contactTextInput.editText!!.text.toString().isBlank()) {
            contactTextInput.error = "연락처를 입력해 주세요."
            isValidation = false

        } else {
            contactTextInput.error = null
            contactTextInput.isErrorEnabled = false
        }

        if (contentTextInput.editText!!.text.toString().isBlank()) {
            contentTextInput.error = "상세 설명을 입력해 주세요."
            isValidation = false

        } else {
            contentTextInput.error = null
            contentTextInput.isErrorEnabled = false
        }
    }

    return isValidation
}
}