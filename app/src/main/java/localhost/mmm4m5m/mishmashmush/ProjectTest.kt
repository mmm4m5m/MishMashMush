@file:Suppress("ClassName")

package localhost.mmm4m5m.mishmashmush

//import android.app.Activity
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlin.system.measureTimeMillis
import timber.log.Timber


/** Helper Singleton
When used - minimal changes/additions in the code
If not used - minimum artifacts in the code
*/
@Suppress("PrivatePropertyName", "FunctionName", "PropertyName", "SimplifyBooleanWithConstants")
open class ProjectTest {
    private val TOAST        = true

    private val LOG          = true
            val USE_Timber   = false
    private val LOG_Timber   = USE_Timber && false
    private val LOG_Prefix   = "_PRJTST"

    private val LOG_Activity = LOG && true
    private val LOG_Fragment = LOG && true
            val LOG_Tests    = LOG && false


    /** Project specific << */
    private val TEST         = true          // TODO disable all tests !!!
            val TEST_Old     = TEST && false // old things //??? todo remove Old - AboutActivity, menu and handlers
            val TEST_Dice    = TEST && true  // auto enter name
            val TEST_Game    = TEST && true  // do not shuffle, auto check answer
            val TEST_Intent  = TEST && false // intent without application
    /** Project specific >> */


    private var logPerformance = false

    fun Toast(context: Context, msg: String) {
        val p: Boolean
        p = PRJTST?.LOG ?: true
        if (TOAST) Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun Toast(context: Context, resId: Int) {
        if (TOAST) Toast(context, "%s=%s (%s)".format
            (context.resources.getResourceTypeName(resId)
            ,context.resources.getResourceEntryName(resId)
            ,context.resources.getResourceName(resId)))
    }


    /** Log function - log Throwable in red, log Exception with call stack
    Example: Log(Who) is called from ClassWhere.Method (FileWhere.kt)
    Log: time PID/com.app.name I/TAG: ClassWho.Method Arg1 Arg2 (FileWhere.kt/ClassWhere.Method)

    Example: ClassThis extends ClassWhere. Log(This) is called from ClassWhere.Method (ClassWhere.kt)
    Log: time PID/com.app.name I/TAG: ClassThis.Method Arg1 Arg2 (ClassWhere)
    */
    @Suppress("ConvertToStringTemplate")
    fun Log(obj: Any?, callback: (() -> Any)?, vararg args: Any) {
        //??? todo kotlinize, add comments
        if (!LOG) return
        // who (obj) - whoClassName .whoMethodName
        val tc = obj?.javaClass?.name?.split('.')?.last()
        var tm = '.'+callback?.javaClass?.enclosingMethod?.name.toString()

        // get where the log function is called
        val st = Throwable().stackTrace // like Timber
        var wc: String
        var off = -1
        do { // skip this:class entries (ProjectTest.Log functions)
            off++
            wc = st[2+off].toString().substringBeforeLast('(').substringBeforeLast('.')
        } while (wc == this::class.qualifiedName)

        // where - whereFile/ whereClassName .whereMethodName // where the log function is called
        wc = wc.substringAfterLast('.')
        var wf = st[2+off].fileName+'/'
        var wm = '.'+st[2+off].methodName
        if (logPerformance) return

        // skip duplicate output - whoClassName = whereFileName = whereClassName and whoMethodName = whereMethodName
        if (wf.substringBeforeLast('.') == wc) wf = ""
        if (tc == wc) wc = ""
        if (tm == wm) wm = ""
        if (wf+wc+wm != "") wm = " ("+wf+wc+wm+')'
        tm = tc+tm

        // get arguments
        var ar = ""
        var ex: Throwable? = null
        for (i in args.indices) {
            if (args[i] is Throwable) {
                ar = ' '+(args[i] as Throwable).message.toString()
                // use first Exception, if any. Or first Throwable
                //??? todo separate log line for every Throwable and Exception
                if (ex == null) ex = args[i] as Throwable
                else if (ex::class == Throwable::class && args[i]::class != Throwable::class) ex = args[i] as Throwable
            } else ar += ' '+args[i].toString()
        }

        /** log the message (Probably String.format is one bit slower)
        Timber is providing less information and will add small (useless) overhead
        It will log this file/class always because 'where' is here - the log function is called here
        */
        @SuppressLint("LogNotTimber")
        if (LOG_Timber) when {
            ex == null                    -> Timber.i(LOG_Prefix, tm, ar, wm)
            ex::class == Throwable::class -> Timber.e(LOG_Prefix, tm, ar, wm)
            else                          -> Timber.e(ex, LOG_Prefix, tm, ar, wm)
        } else when {
            ex == null                    -> Log.i(LOG_Prefix, tm+ar+wm)
            ex::class == Throwable::class -> Log.e(LOG_Prefix, tm+ar+wm)
            else                          -> Log.e(LOG_Prefix, tm+ar+wm, ex)
        }
    }

    fun Log(callback: () -> Any, vararg args: Any) {
        Log(callback(), callback = callback, *args)
    }

    fun Log(vararg args: Any) {
        Log(null, null, *args)
    }

    fun LogPerf(callback: () -> Any, msg1: String, time1: Long, msg2: String, time2: Long) {
        Log(callback(), callback = callback, "Performance: %s=%s ms VS %s=%s ms".format(msg1, time1, msg2, time2))
    }

    fun LogActivity(callback: () -> Any, vararg args: Any) {
        if (LOG_Activity) Log(callback(), callback = callback, *args)
    }

    fun LogFragment(callback: () -> Any, vararg args: Any) {
        if (LOG_Fragment) Log(callback(), callback = callback, *args)
    }

    @Suppress("ConvertToStringTemplate")
    @SuppressLint("LogNotTimber")
    private fun _Log_using_stackTrace(msg: Any) { //??? todo remove Old
        if (!LOG) return
        //val st = Thread.currentThread().stackTrace[5]
        val st = Throwable().stackTrace[3] // like Timber
        val c =
            //"("+Thread.currentThread().stackTrace[6].className.split(".").last()+") "+
            "("+Throwable().stackTrace[4].className.split(".").last()+") "+
            st.className.split(".").reversed()[0]
        val mn = st.methodName
        var ar = msg.toString()
        if (ar != "") ar = " "+ar
        if (logPerformance) return
        Log.i(LOG_Prefix, c+"."+mn+ar)
    }

    fun _Log_tests(callback: () -> Any) {
        val obj = callback()
        //Log(obj, callback = callback, "msg", 123)
        //Log(callback, "msg", 123)
        Log(obj, null, "msg", 123)
        Log(obj, null, "msg")
        Log(obj, null)
        Log({}, "msg", 123)
        Log({}, "msg")
        Log({})
        Log("msg", 123)
        Log("msg")
        Log()
        Log(Throwable("msg"))
        Log("msg", Throwable("ops"), Exception("msg"))
    }

    fun _Log_check_performance(callback: () -> Any) {
        _Log_using_stackTrace("test")
        var t1: Long = 0
        var t2: Long = 0
        logPerformance = true
        try {
            for (i in 0..10000) t1 += measureTimeMillis { Log (null, callback, "test $i") } // 80-120ms
            for (i in 0..10000) t2 += measureTimeMillis { _Log_using_stackTrace("test $i") } // 300-350ms
        } finally {
            logPerformance = false
        }
        LogPerf(callback, "Log", t1, "using_stackTrace", t2)
    }
}


/**
Activity Lifecycle (Unfocus - press Share/show Intent)
    Open +F+S   : onCreate -              - onStart - onResume
    Unfocus     :          -              -         -          - onPause
    Focus       :          -              -         - onResume
    Hide        :          -              -         -          - onPause - onStop
    Show        :          - onRestart    - onStart - onResume
    U+H+ Close  :          -              -         -          - onPause - onStop -                  onDestroy
Fragments Lifecycle (Unfocus - press Share/show Intent)
    Open +F+S  #: onAttach..onViewCreated - onStart - onResume
    Unfocus    #:                         -         -          - onPause
    Focus      #:                         -         - onResume
    Hide       #:                         -         -          - onPause - onStop
    Show       #:                         - onStart - onResume
    U+H+ Close #:                         -         -          - onPause - onStop - onDestroyView(#) onDestroy(#) onDetach(#) [onDestroy(1) onDetach(1)]
Fragments Navigation
    Fragment   1:                      + onAttach onCreate onCreateView..onStart  -                                           + onResume
    Fragment   2: onPause(1) onStop(1) + onAttach onCreate onCreateView..onStart  - onDestroyView(1)                          + onResume
    Fragment   3: onPause(2) onStop(2) + onAttach onCreate onCreateView..onStart  - onDestroyView(2) onDestroy(2) onDetach(2) + onResume
    Fragment   1: onPause(3) onStop(3) +                   onCreateView..onStart  - onDestroyView(3) onDestroy(3) onDetach(3) + onResume
    Fragments Events: onCreateView..onStart = onCreateView - onViewCreated - onStart
*/

/** Custom Override - middle class
*/
open class ProjectTest_AppCompatActivity : androidx.appcompat.app.AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        PRJTST?.LogActivity({this})
        super.onCreate(savedInstanceState)

        if (PRJTST?.LOG_Tests != true) return
        PRJTST._Log_tests{this}
        PRJTST._Log_check_performance{this}
    }

    override fun onDestroy() {
        PRJTST?.LogActivity({this})
        super.onDestroy()
    }

    override fun onStart() {
        PRJTST?.LogActivity({this})
        super.onStart()
    }

    override fun onRestart() {
        PRJTST?.LogActivity({this})
        super.onRestart()
    }

    override fun onStop() {
        PRJTST?.LogActivity({this})
        super.onStop()
    }

    override fun onResume() {
        PRJTST?.LogActivity({this})
        super.onResume()
    }

    override fun onPause() {
        PRJTST?.LogActivity({this})
        super.onPause()
    }
}


/** Custom Override - middle class
*/
open class ProjectTest_Fragment : androidx.fragment.app.Fragment() {
    //override fun onCreate(savedInstanceState: Bundle?) {
    //    PRJTST?.LogFragment({this})
    //    super.onCreate(savedInstanceState)
    //}

    //override fun onDestroy() {
    //    PRJTST?.LogFragment({this})
    //    super.onDestroy()
    //}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        PRJTST?.LogFragment({this})
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        PRJTST?.LogFragment({this})
        super.onDestroyView()
    }

    //override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    //    PRJTST?.LogFragment({this})
    //    super.onViewCreated(view, savedInstanceState)
    //}

    override fun onAttach(context: Context) {
        PRJTST?.LogFragment({this})
        super.onAttach(context)
    }

    //override fun onAttach(activity: Activity) {
    //    //PRJTST?.LogFragment({this}, "(Activity)")
    //    super.onAttach(activity)
    //}

    override fun onDetach() {
        PRJTST?.LogFragment({this})
        super.onDetach()
    }

    //override fun onStart() {
    //    PRJTST?.LogFragment({this})
    //    super.onStart()
    //}

    //override fun onStop() {
    //    PRJTST?.LogFragment({this})
    //    super.onStop()
    //}

    override fun onResume() {
        PRJTST?.LogFragment({this})
        super.onResume()
    }

    override fun onPause() {
        PRJTST?.LogFragment({this})
        super.onPause()
    }
}
