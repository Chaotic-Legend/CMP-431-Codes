package com.example.artspace.data

import com.example.artspace.R
import com.example.artspace.model.Art

object ArtworkRepository {
    val artworks = listOf(
        Art(
            R.string.art_1_artist,
            R.string.art_1_artist_info,
            R.string.art_1_title,
            R.string.art_1_year,
            R.string.art_1_description,
            R.string.art_1_artist_bio,
            R.drawable.art_1_the_starry_night,
            R.drawable.vincent_van_gogh
        ),

        Art(
            R.string.art_2_artist,
            R.string.art_2_artist_info,
            R.string.art_2_title,
            R.string.art_2_year,
            R.string.art_2_description,
            R.string.art_2_artist_bio,
            R.drawable.art_2_the_mona_lisa,
            R.drawable.leonardo_da_vinci
        ),

        Art(
            R.string.art_3_artist,
            R.string.art_3_artist_info,
            R.string.art_3_title,
            R.string.art_3_year,
            R.string.art_3_description,
            R.string.art_3_artist_bio,
            R.drawable.art_3_wanderer_above_the_sea_of_fog,
            R.drawable.caspar_david_friedrich
        ),

        Art(
            R.string.art_4_artist,
            R.string.art_4_artist_info,
            R.string.art_4_title,
            R.string.art_4_year,
            R.string.art_4_description,
            R.string.art_4_artist_bio,
            R.drawable.art_4_doni_tondo,
            R.drawable.michelangelo
        ),

        Art(
            R.string.art_5_artist,
            R.string.art_5_artist_info,
            R.string.art_5_title,
            R.string.art_5_year,
            R.string.art_5_description,
            R.string.art_5_artist_bio,
            R.drawable.art_5_the_scream,
            R.drawable.edvard_munch
        ),

        Art(
            R.string.art_6_artist,
            R.string.art_6_artist_info,
            R.string.art_6_title,
            R.string.art_6_year,
            R.string.art_6_description,
            R.string.art_6_artist_bio,
            R.drawable.art_6_the_great_wave_off_kanagawa,
            R.drawable.katsushika_hokusai
        ),
    )
}