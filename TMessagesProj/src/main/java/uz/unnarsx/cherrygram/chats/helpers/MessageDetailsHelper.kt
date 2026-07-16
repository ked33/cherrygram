package uz.unnarsx.cherrygram.chats.helpers

import android.content.Context
import org.telegram.messenger.AndroidUtilities
import org.telegram.messenger.LocaleController
import org.telegram.messenger.MessageObject
import org.telegram.messenger.R
import org.telegram.tgnet.TLRPC
import org.telegram.ui.ActionBar.AlertDialog
import org.telegram.ui.ActionBar.BaseFragment
import java.util.Date

object MessageDetailsHelper {
    @JvmStatic
    fun show(fragment: BaseFragment, message: MessageObject) {
        val context: Context = fragment.parentActivity ?: return
        val owner = message.messageOwner
        val lines = StringBuilder()
        fun add(label: String, value: Any?) { if (value != null && value.toString().isNotEmpty() && value.toString() != "0") lines.append(label).append(": ").append(value).append('\n') }
        fun formatDate(timestamp: Int): String = LocaleController.getInstance().formatterStats.format(Date(timestamp * 1000L))
        add("ID", owner.id)
        add("对话 ID", message.dialogId)
        add("发送时间", formatDate(owner.date))
        if (owner.edit_date != 0) add("编辑时间", formatDate(owner.edit_date))
        owner.fwd_from?.date?.takeIf { it != 0 }?.let { add("原始转发时间", formatDate(it)) }
        add("发送者 ID", MessageObject.getFromChatId(owner))
        add("分组 ID", owner.grouped_id)
        add("消息类型", message.type)
        val media = MessageObject.getMedia(owner)
        val document = media?.document
        if (document != null) {
            add("文件名", document.attributes.filterIsInstance<TLRPC.TL_documentAttributeFilename>().firstOrNull()?.file_name)
            add("MIME", document.mime_type)
            add("大小", AndroidUtilities.formatFileSize(document.size))
            add("DC", document.dc_id)
            document.attributes.filterIsInstance<TLRPC.TL_documentAttributeVideo>().firstOrNull()?.let {
                add("视频尺寸", "${it.w}×${it.h}")
                add("时长", "${it.duration} 秒")
            }
        }
        media?.photo?.let { photo ->
            add("DC", photo.dc_id)
            val size = photo.sizes.lastOrNull()
            if (size != null) add("图片尺寸", "${size.w}×${size.h}")
        }
        if (!owner.message.isNullOrEmpty()) add("文本", owner.message)
        AlertDialog.Builder(context)
            .setTitle(LocaleController.getString(R.string.CG_MessageDetails))
            .setMessage(lines.toString().trim())
            .setPositiveButton(LocaleController.getString(R.string.Close), null)
            .show()
    }

    @JvmStatic
    fun showTime(fragment: BaseFragment, message: MessageObject) {
        val context = fragment.parentActivity ?: return
        val owner = message.messageOwner
        val text = buildString {
            append("发送时间：").append(LocaleController.getInstance().formatterStats.format(Date(owner.date * 1000L)))
            if (owner.edit_date != 0) append("\n编辑时间：").append(LocaleController.getInstance().formatterStats.format(Date(owner.edit_date * 1000L)))
            owner.fwd_from?.date?.takeIf { it != 0 }?.let { append("\n原始转发时间：").append(LocaleController.getInstance().formatterStats.format(Date(it * 1000L))) }
        }
        AlertDialog.Builder(context)
            .setTitle(LocaleController.getString(R.string.CG_ShowTimeDetails))
            .setMessage(text)
            .setPositiveButton(LocaleController.getString(R.string.Close), null)
            .show()
    }
}
