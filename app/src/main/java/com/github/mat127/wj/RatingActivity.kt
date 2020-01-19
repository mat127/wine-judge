package com.github.mat127.wj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class RatingActivity : AppCompatActivity(), RatingFragment.OnRatingChangeListener {

    private lateinit var pager: ViewPager

    private val ratingBars = RatingBarsState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupPager()
        this.update()
    }

    private fun setupPager() {
        this.pager = findViewById(R.id.ratingPager)
        this.pager.adapter = RatingPagerAdapter(supportFragmentManager)
        val tabLayout = findViewById<TabLayout>(R.id.ratingTabLayout)
        this.pager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(tabLayout)
        )
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { pager.currentItem = it.position }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private inner class RatingPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getCount() = 4
        override fun getItem(position: Int): Fragment? = when (position) {
            0 -> VisualRatingFragment()
            1 -> NoseRatingFragment()
            2 -> TasteRatingFragment()
            3 -> HarmonyRatingFragment()
            else -> null
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_rating, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_send -> {
            this.sendRating()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    fun onEliminatedClick(view: View) {
        if(this.isEliminatedChecked()) {
            this.setDetailsVisibility(View.INVISIBLE)
            this.setScore(0)
        }
        else {
            this.setDetailsVisibility(View.VISIBLE)
            this.update()
        }
    }

    fun setDetailsVisibility(visibility: Int) {
        findViewById<ViewPager>(R.id.ratingPager)?.visibility = visibility
        findViewById<TabLayout>(R.id.ratingTabLayout)?.visibility = visibility
    }

    override fun onRatingChange(id: Int, rating: Float) {
        this.ratingBars.update(id, rating.toInt())
        this.update()
    }

    private fun update() {
        val rating = this.getRating()
        this.setScore(rating.score)
    }

    private fun setScore(value: Int) {
        findViewById<TextView>(R.id.totalScoreTextView).text = value.toString()
        findViewById<ProgressBar>(R.id.totalScoreProgressBar).progress = value
    }

    private fun getRating(): WineRating {
        val sampleId = getSampleId()
        return if(isEliminatedChecked())
            EliminatedWineRating(sampleId)
        else
            this.ratingBars.getRating(sampleId)
    }

    private fun getSampleId() =
        findViewById<EditText>(R.id.sampleEditText).text.toString()

    private fun isEliminatedChecked() = findViewById<Switch>(R.id.eliminatedSwitch).isChecked

    private fun sendRating() {
        val ratingText = getRating().description
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, ratingText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "wine rating")
        startActivity(shareIntent)
    }

    private class RatingBarsState {

        private val stars = mutableMapOf<Int,Int>()

        fun update(id: Int, stars: Int) {
            this.stars[id] = stars
        }

        fun getStars(id: Int) = this.stars[id] ?: 0

        fun getScore(id: Int) = starsToScore(id, this.getStars(id))

        companion object StarsToScoreConverter {

            val conversion = mapOf(
                R.id.limpidityRatingBar to intArrayOf(0,1,2,3,4,5),
                R.id.otherThanLimpidityRatingBar to intArrayOf(0,2,4,6,8,10),
                R.id.noseGenuinessRatingBar to intArrayOf(0,2,3,4,5,6),
                R.id.nosePositiveIntensityRatingBar to intArrayOf(0,2,4,6,7,8),
                R.id.noseQualityRatingBar to intArrayOf(0,8,10,12,14,16),
                R.id.tasteGenuinessRatingBar to intArrayOf(0,2,3,4,5,6),
                R.id.tastePositiveIntensityRatingBar to intArrayOf(0,2,4,6,7,8),
                R.id.tasteHarmoniousPersistenceRatingBar to intArrayOf(0,4,5,6,7,8),
                R.id.tasteQualityRatingBar to intArrayOf(0,10,13,16,19,22),
                R.id.harmonyOverallJudgementRatingBar to intArrayOf(0,7,8,9,10,11)
            )

            fun starsToScore(id: Int, stars: Int): Int {
                val barConversion = conversion.getValue(id)
                return barConversion[stars]
            }
        }

        fun getRating(id: String) =
            CompletedWineRating(id, getVisual(), getNose(), getTaste(), getHarmony())

        fun getVisual() = Visual(
            this.getScore(R.id.limpidityRatingBar),
            this.getScore(R.id.otherThanLimpidityRatingBar)
        )

        fun getNose() = Nose(
            this.getScore(R.id.noseGenuinessRatingBar),
            this.getScore(R.id.nosePositiveIntensityRatingBar),
            this.getScore(R.id.noseQualityRatingBar)
        )

        fun getTaste() = Taste(
            this.getScore(R.id.tasteGenuinessRatingBar),
            this.getScore(R.id.tastePositiveIntensityRatingBar),
            this.getScore(R.id.tasteHarmoniousPersistenceRatingBar),
            this.getScore(R.id.tasteQualityRatingBar)
        )

        fun getHarmony() = Harmony(
            this.getScore(R.id.harmonyOverallJudgementRatingBar)
        )
    }
}
