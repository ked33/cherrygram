package uz.unnarsx.cherrygram

import android.app.Activity
import org.telegram.messenger.AndroidUtilities
import org.telegram.messenger.ChatObject
import org.telegram.messenger.LocaleController
import org.telegram.messenger.MessagesController
import org.telegram.messenger.R
import org.telegram.messenger.UserConfig
import org.telegram.tgnet.TLRPC
import org.telegram.ui.ActionBar.AlertDialog
import org.telegram.ui.ActionBar.BaseFragment

object Extra {

    const val APP_ID: Int = 0
    const val APP_HASH: String = ""
    const val SMS_HASH: String = ""
    const val ENDPOINT_FOR_DATE: String = "https://example.invalid"
    const val ENDPOINT_FOR_DATE_SECRET: String = ""

    @JvmField val HASH_ARRAY = arrayOf("")
    @JvmField val APP_ARRAY = arrayOf("")
    @JvmField val SMS_ARRAY = arrayOf("")

    @JvmField val Name_Arr = arrayOf("")
    @JvmField val Name_Arr2 = arrayOf("")
    @JvmField val Name_Arr3 = arrayOf("")
    @JvmField val Name_ArrTwo = arrayOf("")
    @JvmField val Name_ArrTwo1 = arrayOf("")
    @JvmField val Name_ArrTwo2 = arrayOf("")
    @JvmField val Name_ArrTwo3 = arrayOf("")
    @JvmField val Name_ArrTwo4 = arrayOf("")
    @JvmField val Name_ArrTwo5 = arrayOf("")
    @JvmField val Name_ArrTwo6 = arrayOf("")
    @JvmField val XName_Arr = arrayOf("")
    @JvmField val XName_ArrTwo = arrayOf("")

    @JvmField val pkg_arrOne = arrayOf("")
    @JvmField val pkg_arrTwo = arrayOf("")
    @JvmField val pkg_arrThree = arrayOf("")
    @JvmField val pkg_hashOne = arrayOf("")
    @JvmField val pkg_hashTwo = arrayOf("")
    @JvmField val pkg_hashThree = arrayOf("")
    @JvmField val pkg_hashGPOne = arrayOf("")
    @JvmField val pkg_hashGPTwo = arrayOf("")
    @JvmField val pkg_hashGPThree = arrayOf("")

    @JvmField val FILE_NAME_HASH = arrayOf("")
    @JvmField val GITLAB_RAW_URL_HASH = arrayOf("")
    @JvmField val FILE_NAME_MARKETPLACE_HASH = arrayOf("")
    @JvmField val GITLAB_RAW_URL_MARKETPLACE_HASH = arrayOf("")
    @JvmField val FILE_NAME_BLOCKED_HASH = arrayOf("")
    @JvmField val GITLAB_RAW_URL_BLOCKED_HASH = arrayOf("")
    @JvmField val FILE_NAME_BADGE_COLORS_HASH = arrayOf("")
    @JvmField val GITLAB_RAW_URL_BADGE_COLORS_HASH = arrayOf("")
    @JvmField val FILE_NAME_TON_RATE_HASH = arrayOf("")
    @JvmField val TON_RATE_URL_HASH = arrayOf("")

    fun getProfileDC(user: TLRPC.User?, chat: TLRPC.Chat?): StringBuilder {
        return StringBuilder()
    }

    fun addBirthdayToCalendar(activity: Activity?, userId: Long) {
    }

    fun getRegistrationDate(fragment: BaseFragment?, activity: Activity?, userId: Long, chatId: Long) {
        if (fragment == null || activity == null || chatId == 0L) return
        val account = fragment.currentAccount
        val controller = MessagesController.getInstance(account)
        val chat = controller.getChat(chatId) ?: return
        val creation = chat.date
        var joined = 0
        val full = controller.getChatFull(chatId)
        full?.participants?.participants?.firstOrNull { it.user_id == UserConfig.getInstance(account).clientUserId }?.let { joined = it.date }
        fun show(joinedDate: Int) {
            val message = StringBuilder()
            if (creation != 0) message.append(LocaleController.formatString(R.string.CG_GroupCreatedAt, LocaleController.formatDateChat(creation)))
            if (joinedDate != 0) {
                if (message.isNotEmpty()) message.append('\n')
                message.append(LocaleController.formatString(R.string.CG_JoinedAt, LocaleController.formatDateChat(joinedDate)))
            } else {
                if (message.isNotEmpty()) message.append('\n')
                message.append(LocaleController.getString(R.string.CG_JoinDateUnavailable))
            }
            AlertDialog.Builder(activity).setTitle(chat.title ?: "").setMessage(message.toString()).setPositiveButton(R.string.Close, null).show()
        }
        if (joined != 0 || !ChatObject.isChannel(chat)) {
            show(joined)
            return
        }
        val request = TLRPC.TL_channels_getParticipant()
        request.channel = controller.getInputChannel(chatId)
        request.participant = controller.getInputPeer(UserConfig.getInstance(account).clientUserId)
        controller.connectionsManager.sendRequest(request) { response, _ ->
            val date = (response as? TLRPC.TL_channels_channelParticipant)?.participant?.date ?: 0
            AndroidUtilities.runOnUIThread { show(date) }
        }
    }
}
