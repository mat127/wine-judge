package com.github.mat127.wrc.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mat127.wrc.R

class HarmonyRatingFragment : RatingFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_harmony_rating, container, false)
    }

    override val ratingBarIds: Iterable<Int> = listOf(
        R.id.harmonyOverallJudgementRatingBar
    )
}
