package org.techtown.petfinder

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.firebase.auth.FirebaseAuth
import org.techtown.fragments.FindPetFragment
import org.techtown.fragments.LostPetFragment
import org.techtown.petfinder.databinding.ActivityBottomNavMainBinding
import java.util.*

class BottomNavMainActivity : BaseActivity() {

    lateinit var binding: ActivityBottomNavMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bottom_nav_main)
        setValues()
        setupEvents()
        initUi()

    }



    private fun initUi() {
        with(binding) {
            (areaTextInput.editText as? AutoCompleteTextView)?.run {
                val adapter = ArrayAdapter(
                    this@BottomNavMainActivity,
                    R.layout.item_spinner_small,
                    resources.getStringArray(R.array.area)
                )

                setAdapter(adapter)
                setText(adapter.getItem(0) as String, false)
            }

            viewPager.isUserInputEnabled = false
            viewPager.adapter = ViewPagerAdapter(this@BottomNavMainActivity)

            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.lostpet -> viewPager.setCurrentItem(0, false)
                    R.id.findpet -> viewPager.setCurrentItem(1, false)
                    else -> viewPager.setCurrentItem(2, false)
                }

                return@setOnItemSelectedListener true
            }
        }
    }

    private class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            if (position == 0) {
                return LostPetFragment()

            } else if (position == 1) {
                return FindPetFragment()

            } else {

                return LostPetFragment()
            }
        }
    }

    override fun onBackPressed() {
        FirebaseAuth.getInstance().signOut()
        super.onBackPressed()
    }
    override fun setupEvents() {
    }



    override fun setValues() {


    }


  }




