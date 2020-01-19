package com.github.mat127.wj

open abstract class WineRating(val sampleId: String) {
    abstract val eliminated: Boolean
    open val score: Int get() = 0
    abstract val description: String
}

class EliminatedWineRating(sampleId: String) : WineRating(sampleId) {
    override val eliminated: Boolean get() = true
    override val description: String
        get() = "sample #$sampleId eliminated"
}

class CompletedWineRating(
    sampleId: String,
    val visual: Visual,
    val nose: Nose,
    val taste: Taste,
    val harmony: Harmony
) : WineRating(sampleId) {

    override val eliminated: Boolean get() = false

    override val score get() = visual.score + nose.score + taste.score + harmony.score

    override val description: String
        get() = "sample #$sampleId score $score"
}

data class Visual(
    val limpidity: Int,
    val other: Int
) {
    val score get() = limpidity + other

    companion object Factory {
        fun create(limpidity: Int?, other: Int?) =
          limpidity?.let { other?.let {
              Visual(limpidity, other)
          }}
    }
}

data class Nose(
    val genuiness: Int,
    val positiveIntensity: Int,
    val quality:Int
) {
    val score get() = genuiness + positiveIntensity + quality

    companion object Factory {
        fun create(genuiness: Int?, positiveIntensity: Int?, quality: Int?) =
            genuiness?.let { positiveIntensity?.let { quality?.let {
                Nose(genuiness, positiveIntensity, quality)
            }}}
    }
}

data class Taste(
    val genuiness: Int,
    val positiveIntensity: Int,
    val harmoniousPersistence: Int,
    val quality:Int
) {
    val score get() = genuiness + positiveIntensity + harmoniousPersistence + quality

    companion object Factory {
        fun create(genuiness: Int?, positiveIntensity: Int?, harmoniousPersistence: Int?, quality: Int?) =
            genuiness?.let { positiveIntensity?.let { harmoniousPersistence?. let { quality?.let {
                Taste(genuiness, positiveIntensity, harmoniousPersistence, quality)
            }}}}
    }
}

data class Harmony(
    val overallJudgement: Int
) {
    val score get() = overallJudgement

    companion object Factory {
        fun create(overall: Int?) = overall?.let { Harmony(overall) }
    }
}