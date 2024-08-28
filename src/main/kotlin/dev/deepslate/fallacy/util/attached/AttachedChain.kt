package dev.deepslate.fallacy.util.attached

data class AttachedChain<T>(val base: T, val attachments: List<Attached<T>>) {
    fun apply() = attachments.fold(base) { acc, attached -> attached.attach(acc) }

    fun updateBase(newBase: T) = copy(base = newBase)

    fun updateAttachments(newAttachments: List<Attached<T>>) = copy(attachments = newAttachments)

    fun addAttachment(attachment: Attached<T>) = copy(attachments = attachments + attachment)

    //用于显示说明文本
    val chatComponents = attachments.map { it.chatComponent }
}