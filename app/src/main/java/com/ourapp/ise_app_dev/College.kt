package com.ourapp.ise_app_dev

data class College(
    val name: String,        // The name of the college, e.g., "IIT Bombay"
    val nirfRank: Int,       // The NIRF ranking of the college, e.g., 1, 2, etc.
    val state: String,       // The state in which the college is located, e.g., "Maharashtra"
    val logoUrl: String      // A URL to the college logo image
)