package org.techtown.extensions

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import org.techtown.petfinder.BuildConfig
import org.techtown.petfinder.R
import splitties.resources.styledColor

// 이미지 읽기 쓰기 권한 관리? 와 fishbun 라이브러리의 역할을 해주는 파일 인듯..
fun Activity.showImagePicker(maxCount: Int) {
    Dexter.withActivity(this)
        .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report?.areAllPermissionsGranted() == true) {
                    val colorPrimary: Int = styledColor(R.attr.colorPrimary)
                    val colorSecondary: Int = styledColor(R.attr.colorSecondary)
                    val colorOnSecondary: Int = styledColor(R.attr.colorOnSecondary)
                    val colorSurface: Int = styledColor(R.attr.colorSurface)
                    val colorOnSurface: Int = styledColor(R.attr.colorOnSurface)

                    val backDrawable: Drawable =
                        AppCompatResources.getDrawable(this@showImagePicker, R.drawable.ic_baseline_arrow_back_24)!!.mutate()
                    backDrawable.setTint(colorOnSurface)

                    val doneDrawable: Drawable =
                        AppCompatResources.getDrawable(this@showImagePicker, R.drawable.ic_baseline_done_24)!!.mutate()
                    doneDrawable.setTint(colorOnSurface)

                    val allDoneDrawable: Drawable =
                        AppCompatResources.getDrawable(this@showImagePicker, R.drawable.ic_baseline_done_all_24)!!.mutate()
                    allDoneDrawable.setTint(colorOnSurface)

                    FishBun.with(this@showImagePicker)
                        .setImageAdapter(GlideAdapter())
                        .setIsUseDetailView(false)
                        .setPickerSpanCount(4)
                        .setActionBarColor(colorSurface, colorPrimary, false)
                        .setActionBarTitleColor(colorOnSurface)
                        .setHomeAsUpIndicatorDrawable(backDrawable)
                        .setAlbumSpanCount(1, 2)
                        .setButtonInAlbumActivity(false)
                        .hasCameraInPickerPage(true)
                        .setReachLimitAutomaticClose(true)
                        .setDoneButtonDrawable(doneDrawable)
                        .setAllDoneButtonDrawable(allDoneDrawable)
                        .setSelectCircleBackgroundColor(colorSecondary)
                        .setSelectCircleStrokeColor(Color.WHITE)
                        .setSelectCircleTextColor(colorOnSecondary)
                        .setMaxCount(maxCount)
                        .setMinCount(1)
                        .startAlbum()

                } else {
                    val dialog = AlertDialog.Builder(this@showImagePicker)
                        .setMessage("권한을 거부하시면 사진을 가져올 수 없습니다.\n\n설정 > 권한에서 저장 권한을 켜 주세요.")
                        .setPositiveButton("설정") { _: DialogInterface?, _: Int ->
                            startActivity(
                                Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                                )
                            )
                        }
                        .setNegativeButton("취소") { _: DialogInterface?, _: Int -> }
                        .create()

                    dialog.show()
                }
            }

            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                val dialog = AlertDialog.Builder(this@showImagePicker)
                    .setMessage("파일을 읽고 쓰기 위한 권한이 필요합니다.")
                    .setPositiveButton("확인") { _: DialogInterface?, _: Int -> token!!.continuePermissionRequest() }
                    .setNegativeButton("취소") { _: DialogInterface?, _: Int -> token!!.cancelPermissionRequest() }
                    .create()

                dialog.show()
            }
        })
        .check()
}