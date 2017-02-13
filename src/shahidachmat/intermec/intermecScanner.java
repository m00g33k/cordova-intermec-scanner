package shahidachmat.intermec;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaInterface;
import android.util.Log;
import android.provider.Settings;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.intermec.aidc.*;

public class intermecScanner extends CordovaPlugin implements BarcodeReadListener {

    public static final String TAG = "SCANNER.INTERMEC";
    private com.intermec.aidc.BarcodeReader bcr;
    private com.intermec.aidc.VirtualWedge wedge;
    final int duration = Toast.LENGTH_SHORT;
    private String message = "";
    private CallbackContext callbackContextReference;
    /**
     * Constructor.
     */
    public intermecScanner() {}

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        // Make sure the BarcodeReader dependent service is connected and
        // register a callback to service connect and disconnect events.
        AidcManager.connectService(cordova.getActivity().getApplicationContext(), new AidcManager.IServiceListener() {
            public void onConnect()
            {
              // The depended service is connected and it is ready
              // to receive bar code requests and virtual wedge
              try {
                       //Initial bar code reader instance
                 bcr = new BarcodeReader();

                 //disable virtual wedge
                 wedge = new VirtualWedge();
                 wedge.setEnable(false);
                 Log.v(TAG,"Init intermecScanner");

               } catch (BarcodeReaderException e)
               {
                 e.printStackTrace();
               }
                     catch (VirtualWedgeException e)
               {
                 e.printStackTrace();
               }

            }



            public void onDisconnect()
            {
            }



        });
    }

    public boolean execute(final String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        // Shows a toast
        Log.v(TAG, " received:" + action);
        if (action.equals("ACTIVATE")) {
          message = "Scanner Activated";
          try {
      			if(bcr != null)
      			{
      				//enable scanner
      				bcr.setScannerEnable(true);

      				//set listener
      				bcr.addBarcodeReadListener(this);
              callbackContextReference = callbackContext;
      			} else {
              /*cordova.getActivity().runOnUiThread(new Runnable() {
                  public void run() {
                      Toast toast = Toast.makeText(cordova.getActivity().getApplicationContext(), "ERROR CONNECTING TO READER.", duration);
                      toast.show();
                  }
              });*/
            }

      		} catch (BarcodeReaderException e) {
      			e.printStackTrace();
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast toast = Toast.makeText(cordova.getActivity().getApplicationContext(), "ERROR READING BARCODE", duration);
                    toast.show();
                }
            });
      		}
        } else if (action.equals("DEACTIVATE")) {
          message = "Scanner Deactivated";
          if(bcr != null)
          {
            try {
               bcr.setScannerEnable(false);

             } catch (BarcodeReaderException e)
             {
               e.printStackTrace();
             }
          }
          //disconnect from data collection service
          //AidcManager.disconnectService();  If you wanna kill the world and enjoy it :)

        } else {
          message = "Scanner Invoked";
        }
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast toast = Toast.makeText(cordova.getActivity().getApplicationContext(), message, duration);
                toast.show();
            }
        });
        return true;
    }
    @Override
    public void barcodeRead(BarcodeReadEvent aBarcodeReadEvent)
    {
          String strDeviceId =  aBarcodeReadEvent.getDeviceId();
          String strBarcodeData =  aBarcodeReadEvent.getBarcodeData();
          String strSymbologyId = aBarcodeReadEvent.getSymbolgyId();
          message = strBarcodeData;
          cordova.getActivity().runOnUiThread(new Runnable() {
              public void run() {
                  Toast toast = Toast.makeText(cordova.getActivity().getApplicationContext(), message, duration);
                  toast.show();
                  PluginResult result_callback = new PluginResult(PluginResult.Status.OK, message);
                  result_callback.setKeepCallback(true);
                  callbackContextReference.sendPluginResult(result_callback);
              }
          });
     }
}
