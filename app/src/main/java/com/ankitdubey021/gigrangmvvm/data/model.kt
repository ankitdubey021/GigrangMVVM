package com.ankitdubey021.gigrangmvvm.data

import com.google.gson.annotations.SerializedName

data class Category(
    val id : Int,
    val title : String,
    var image : String?
)

data class DeveloperList(
    @SerializedName("results")
    var data: MutableList<Developer>
)

data class Developer(
    var user_id : Int,
    val email :String,
    val firstname : String,
    val lastname : String,
    var gender : String?,
    var mobile_no : String?,
    @SerializedName("img_url")
    var profilePic: String?,
    var city : String?,
    var bio : String?,
    val experience : List<Experience>?,
    val project : List<Project>?,
    val education : List<Education>?,
    val skillsinfo : List<SkillInfo>?,
    val price : Price?
) {
    data class Experience(
        val title: String,
        val start_date:String,
        val end_date:String,
        val company_name:String,
        val city:String,
        val experience_in_years : Double
    )

    data class Price(val charges : Long)

    data class Project(
        val project_name : String,
        val about:String?,
        val technology : List<String>
    )

    data class Education(
        val university : String,
        val degree:String,
        val major:String,
        val start_date:String,
        val end_date : String
    )

    data class SkillInfo(
        val id: Int,
        val skill: Skill
    ){
        data class Skill(
            val id: Int,
            val category_id : Int,
            val title : String
        )
    }
}