package com.github.mat127.wj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*

class RatingActivity : AppCompatActivity() {

    private val starToScore = mapOf(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)
        setSupportActionBar(findViewById(R.id.toolbar))
        for(id in starToScore.keys) {
            findViewById<RatingBar>(id)
                .setOnRatingBarChangeListener {
                        ratingBar, rating, fromUser -> update()
                }
        }
        this.update()
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
        val details = findViewById<ScrollView>(R.id.detailsScrollView)
        if(this.isEliminatedChecked()) {
            details.visibility = View.INVISIBLE
            this.setScore(0)
        }
        else {
            details.visibility = View.VISIBLE
            this.update()
        }
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
        val sampleNumber = getSampleNumber()
        return if(isEliminatedChecked())
            EliminatedWineRating(sampleNumber)
        else
            CompletedWineRating(sampleNumber,
                getVisual(),
                getNose(),
                getTaste(),
                getHarmony()
            )
    }

    private fun getSampleNumber(): Int {
        val text = findViewById<EditText>(R.id.sampleNumberEditText).text.toString()
        return text.toIntOrNull() ?: 0
    }

    private fun isEliminatedChecked() = findViewById<CheckBox>(R.id.eliminatedCheckBox).isChecked

    private fun getVisual() = Visual(
        getRating(R.id.limpidityRatingBar),
        getRating(R.id.otherThanLimpidityRatingBar)
    )

    private fun getNose() = Nose(
        getRating(R.id.noseGenuinessRatingBar),
        getRating(R.id.nosePositiveIntensityRatingBar),
        getRating(R.id.noseQualityRatingBar)
    )

    private fun getTaste() = Taste(
        getRating(R.id.tasteGenuinessRatingBar),
        getRating(R.id.tastePositiveIntensityRatingBar),
        getRating(R.id.tasteHarmoniousPersistenceRatingBar),
        getRating(R.id.tasteQualityRatingBar)
    )

    private fun getHarmony() = Harmony(
        getRating(R.id.harmonyOverallJudgementRatingBar)
    )

    private fun getRating(ratingBarId: Int): Int {
        val starScore = findViewById<RatingBar>(ratingBarId).rating.toInt()
        return starToScore[ratingBarId]?.get(starScore) ?: 0
    }

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
}
