package com.example.vaccineslotchecker

data class centerModal(
    val centerName      : String,
    val centerAddress   : String,
    val centerFromTime  : String,
    val centerToTime    : String,
    val fee_type        : String,
    val ageLimit        : Int,
    val vaccineName     : String,
    val availableCapacity : Int
)
