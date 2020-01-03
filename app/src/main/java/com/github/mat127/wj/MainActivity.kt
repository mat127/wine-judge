package com.github.mat127.wj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_main)
        for(id in starToScore.keys) {
            findViewById<RatingBar>(id)
                .setOnRatingBarChangeListener {
                        ratingBar, rating, fromUser -> updateRating()
                }
        }
        this.updateRating()
    }


    private fun updateRating() {
        val rating = this.calculateRating()
        this.setRating(rating)
    }

    private fun setRating(value: Int) {
        findViewById<TextView>(R.id.totalScoreTextView).text = value.toString()
        findViewById<ProgressBar>(R.id.totalScoreProgressBar).progress = value
    }

    private fun calculateRating() = starToScore.keys.fold(0,
        { sum, ratingBarId -> sum + calculateRating(ratingBarId) }
    )

    private fun calculateRating(ratingBarId: Int): Int {
        val starScore = findViewById<RatingBar>(ratingBarId).rating.toInt()
        val conversion = starToScore[ratingBarId]?.joinToString(",", "[", "]")
        println("$ratingBarId, $starScore, $conversion")
        return starToScore[ratingBarId]?.get(starScore) ?: 0
    }
}
