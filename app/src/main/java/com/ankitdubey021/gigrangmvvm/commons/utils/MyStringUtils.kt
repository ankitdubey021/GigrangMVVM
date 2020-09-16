package com.ankitdubey021.gigrangmvvm.commons.utils

import com.ankitdubey021.gigrangmvvm.data.Developer
import java.util.*

class MyStringUtils {

    companion object{
        fun join(s1:String,s2:String) = "$s1 $s2"
        fun joinByDelimeter(s1:String,s2:String) = "$s1 - $s2"

        fun totalExperience(list:List<Developer.Experience>?):String{
            val total = list?.fold(0.0){sum, element -> sum + element.experience_in_years }?:0
            return "$total years experience"
        }

        fun skills(list: List<Developer.SkillInfo>?):String{
            if(list==null) return ""
            val skillList = mutableListOf<String>()
            list.forEach { skillList.add(it.skill.title) }
            return skillList.joinToString(", ")
        }

        fun anyToString(num : Any) = num.toString()
        fun salary(num : Long) = "\u20B9$num"
        fun listByCommaSeperated(list:List<String>?) = list?.joinToString(separator = ", ")?:""

    }
}