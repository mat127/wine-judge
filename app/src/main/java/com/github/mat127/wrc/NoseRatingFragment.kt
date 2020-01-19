package com.github.mat127.wrc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class NoseRatingFragment : RatingFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nose_rating, container, false)
    }

    override val ratingBarIds: Iterable<Int> = listOf(
        R.id.noseGenuinessRatingBar,
        R.id.nosePositiveIntensityRatingBar,
        R.id.noseQualityRatingBar
    )
}
