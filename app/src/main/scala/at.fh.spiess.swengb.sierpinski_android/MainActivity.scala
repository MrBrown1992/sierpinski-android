package at.fh.spiess.swengb.sierpinski_android

import android.app.Activity
import android.os.Bundle

class MainActivity extends Activity {
  private var sierpinskiCanvas: SierpinskiCalculation = null


  override protected def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    sierpinskiCanvas = findViewById(R.id.SierpinskiView).asInstanceOf[SierpinskiCalculation]
  }
}
