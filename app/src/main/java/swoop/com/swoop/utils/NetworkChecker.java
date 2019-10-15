package swoop.com.swoop.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashSet;
import java.util.Set;

public class NetworkChecker extends BroadcastReceiver{

    private static Set<NetworkChangeListener> networkChangeListeners;
    private Boolean connected;

    public NetworkChecker(){
        networkChangeListeners = new HashSet<NetworkChangeListener>();
        connected = null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null || intent.getExtras() == null)
        {
            return;
        }

        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null)
        {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null && info.getState() == NetworkInfo.State.CONNECTED)
            {
                connected = true;
            }
            else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE))
            {
                connected = false;
            }
        }

        notifyStateToAll();

    }

    private void notifyStateToAll(){
        for (NetworkChangeListener listener : networkChangeListeners)
        {
            notifyState(listener);
        }
    }

    private void notifyState(NetworkChangeListener listener) {
        if (connected)
        {
            listener.networkAvailable();
        }
        else
        {
            listener.networkUnavailable();
        }
    }

    public void addListener(NetworkChangeListener listener){
        networkChangeListeners.add(listener);
    }

    public void removeListener(NetworkChangeListener listener){
        networkChangeListeners.remove(listener);
    }

    public interface NetworkChangeListener{
        void networkAvailable();
        void networkUnavailable();
    }
}
