package com.github.mat127.wj

open abstract class WineRating(val sampleId: String) {
    abstract val eliminated: Boolean
    open val score: Int get() = 0
    abstract val description: String

    companion object StarsToScoreConverter {

        private val limpidityStarScore = intArrayOf(0,1,2,3,4,5)
        fun starsToLimpidity(stars: Int) = limpidityStarScore[stars]

        private val otherThanLimpidityStarScore = intArrayOf(0,2,4,6,8,10)
        fun starsToOtherThanLimpidity(stars: Int) = otherThanLimpidityStarScore[stars]

        private val noseGenuinessStarScore = intArrayOf(0,2,3,4,5,6)
        fun starsToNoseGenuiness(stars: Int) = noseGenuinessStarScore[stars]

        private val nosePositiveIntensityStarScore = intArrayOf(0,2,4,6,7,8)
        fun starsToNosePositiveIntensity(stars: Int) = nosePositiveIntensityStarScore[stars]

        private val noseQualityStarScore = intArrayOf(0,8,10,12,14,16)
        fun starsToNoseQuality(stars: Int) = noseQualityStarScore[stars]

        private val tasteGenuinessStarScore = intArrayOf(0,2,3,4,5,6)
        fun starsToTasteGenuiness(stars: Int) = tasteGenuinessStarScore[stars]

        private val tastePositiveIntensityStarScore = intArrayOf(0,2,4,6,7,8)
        fun starsToTastePositiveIntensity(stars: Int) = tastePositiveIntensityStarScore[stars]

        private val tasteHarmoniousPersistenceStarScore = intArrayOf(0,4,5,6,7,8)
        fun starsToTasteHarmoniousPersistence(stars: Int) = tasteHarmoniousPersistenceStarScore[stars]

        private val tasteQualityStarScore = intArrayOf(0,10,13,16,19,22)
        fun starsToTasteQuality(stars: Int) = tasteQualityStarScore[stars]

        private val harmonyOverallJudgementStarScore = intArrayOf(0,7,8,9,10,11)
        fun starsToHarmonyOverallJudgement(stars: Int) = harmonyOverallJudgementStarScore[stars]
    }
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