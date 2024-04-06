package com.example.scala

import com.example.scala.MessageType.MessageType

class Message(private val message: String, val messageType: MessageType) {
  //def getMessage(): String = message
  //def getType(): MessageType = messageType

  override def toString: String = s"$messageType: $message"
}

object MessageType extends Enumeration {
  type MessageType = Value
  val INFO, WARNING, ERROR = Value
}