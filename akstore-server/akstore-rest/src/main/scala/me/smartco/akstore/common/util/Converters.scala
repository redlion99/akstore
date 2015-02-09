package me.smartco.akstore.common.util

import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by libin on 14-11-20.
 */
class Converters {
  var sdf=new SimpleDateFormat("MM/dd/yyyy")
  implicit def stringToDate(x: String) :Date = {
    if(x.isEmpty)
      null
    else
      sdf.parse(x)
  }
}


object Converters extends Converters