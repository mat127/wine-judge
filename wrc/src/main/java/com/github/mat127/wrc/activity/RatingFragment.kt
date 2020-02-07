package com.github.mat127.wrc.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import androidx.fragment.app.Fragment

abstract class RatingFragment : Fragment(), RatingBar.OnRatingBarChangeListener {

    private var listener: OnRatingChangeListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnRatingChangeListener)
            listener = context
        else
            throw RuntimeException(context.toString() + " must implement OnNoseRatingChangeListener")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.listen()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnRatingChangeListener {
        fun onRatingChange(id: Int, rating: Float)
    }

    override fun onRatingChanged(bar: RatingBar?, rating: Float, fromUser: Boolean) {
        bar?.let { listener?.onRatingChange(it.id, rating) }
    }

    abstract val ratingBarIds: Iterable<Int>

    private fun listen() {
        this.ratingBarIds
            .map { this.getRatingBar(it) }
            .forEach { it!!.setOnRatingBarChangeListener(this)}
    }

    fun getRatingBar(id: Int) = view?.findViewById<RatingBar>(id)

    fun getStars(id: Int) = this.getRatingBar(id)?.numStars
}