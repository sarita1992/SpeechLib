package com.sarita.speechlibrary

public interface OutputListner {
 fun result(data:String)
 fun listening(data:String)
 fun error(data:String)
}