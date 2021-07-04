// CONFIG TEST - helper Singleton and custom Overrides
// TODO disable TEST !!!
package localhost.mmm4m5m.mishmashmush

val PRJTST: localhost.mmm4m5m.mishmashmush.ProjectTest? = null ?: localhost.mmm4m5m.mishmashmush.ProjectTest()
open class AppCompatActivity : localhost.mmm4m5m.mishmashmush.ProjectTest_AppCompatActivity() {}
open class Fragment : localhost.mmm4m5m.mishmashmush.ProjectTest_Fragment() {}

//??? todo kotler do not have conditional compile/define/macros
//??? todo investigate - add config from src dir - gradle: sourceSets['main'].java.srcDirs += 'src/main/kotlin-config-test'
//??? todo investigate - ignore config file - gradle: java.exclude '**/ConfigTest.kt'
