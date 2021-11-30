package org.techtown.petfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import org.techtown.petfinder.databinding.ActivityBottomNavMainBinding
import java.util.*

class BottomNavMainActivity : BaseActivity() {

    lateinit var binding: ActivityBottomNavMainBinding
    val viewList = ArrayList<View>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bottom_nav_main)
        setValues()
        setupEvents()
        viewList.add(layoutInflater.inflate(R.layout.lost_pet_fragment, null))
        viewList.add(layoutInflater.inflate(R.layout.find_pet_fragment, null))
        viewList.add(layoutInflater.inflate(R.layout.activity_review, null))
        binding.viewPager.adapter = pagerAdapter()

//        binding.viewPager.addOnAdapterChangeListener(object :
//            ViewPager.SimpleOnPageChangeListener() {
//            override fun onPageSelected(position: Int) {
//                when (position) {
//                    0 -> binding.bottomNavigationView.selectedItemId = R.id.lostPet
//                    1 -> binding.bottomNavigationView.selectedItemId = R.id.findPet
//                    2 -> binding.bottomNavigationView.selectedItemId = R.id.review
//
//                }
//
//
//            }
//        })
//        binding.bottomNavigationView.setOnNavigationItemReselectedListener {
//        when(it.itemId){
//
//            R.id.lostPet -> binding.viewPager.setCurrentItem(0)
//            R.id.findPet -> binding.viewPager.setCurrentItem(1)
//            R.id.review -> binding.viewPager.setCurrentItem(2)
//
//
//        }
//            return@setOnNavigationItemReselectedListener
//
//
//
//        }
    }
    inner class pagerAdapter : PagerAdapter() {
        override fun getCount() = viewList.size

        override fun isViewFromObject(view: View, `object`: Any) = view == `object`

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var currentView = viewList[position]
            binding.viewPager.addView(currentView)
            return currentView

        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            binding.viewPager.removeView(`object` as View)
        }
    }


    override fun setupEvents() {

    }

    override fun setValues() {

    }
}


