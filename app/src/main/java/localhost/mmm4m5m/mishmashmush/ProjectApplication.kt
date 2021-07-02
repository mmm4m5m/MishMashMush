package localhost.mmm4m5m.mishmashmush

import android.content.Context
import android.view.View
import android.widget.Toast

const val PROJECT_TEST                = true // TODO disable tests!
const val PROJECT_TEST_ABOUT_ACTIVITY = PROJECT_TEST && false //??? todo remove AboutActivity (menu and handlers)
const val PROJECT_TEST_GAME_ACTIVITY  = PROJECT_TEST && false //??? todo remove GameActivity (menu and handlers)
const val PROJECT_TEST_DICE           = PROJECT_TEST && true
const val PROJECT_TEST_GAME           = PROJECT_TEST && true

fun PROJECT_TEST_TOAST(context: Context, msg: String) {
    if (!PROJECT_TEST) return
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}

fun PROJECT_TEST_TOAST(context: Context, resId: Int) {
    if (!PROJECT_TEST) return
    val t = context.resources.getResourceTypeName(resId)
    val e = context.resources.getResourceEntryName(resId)
    val n = context.resources.getResourceName(resId)
    //val p = context.resources.getResourcePackageName(resId)
    PROJECT_TEST_TOAST(context, "%s=%s (%s)".format(t, e, n))
}

//class ProjectApplication {
//}
