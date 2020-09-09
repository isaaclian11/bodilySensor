package com.iastate.bodilysensonble.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.iastate.bodilysensonble.R;

import java.util.ArrayList;

public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListAdapter.ViewHolder> {

    ArrayList<BleDevice> mBleDevices;

    Context context;

    public BluetoothListAdapter(Context context){
        this.context = context;
        mBleDevices = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.ble_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final BleDevice currentDevice = mBleDevices.get(position);
            if(currentDevice!=null){
                String name = currentDevice.getName();
                String id = currentDevice.getMac();
                holder.device_name.setText(name);
                holder.device_id.setText(id);
            }
            if(BleManager.getInstance().isConnected(currentDevice)){
                holder.btn_connect.setVisibility(View.GONE);
                holder.device_connected.setVisibility(View.VISIBLE);
            }else{
                holder.btn_connect.setVisibility(View.VISIBLE);
                holder.device_connected.setVisibility(View.GONE);
            }

            holder.btn_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        mListener.onConnect(currentDevice);
                    }
                }
            });

            holder.btn_disconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        mListener.onDisConnect(currentDevice);
                    }
                }
            });


    }

    @Override
    public int getItemCount() {
        return mBleDevices.size();
    }

    public void add(BleDevice device){
        mBleDevices.add(device);
    }

    public void remove(BleDevice device){
        for(int i=0; i<mBleDevices.size(); i++){
            if(mBleDevices.get(i).getKey().equals(device.getKey())){
                mBleDevices.remove(i);
            }
        }
    }

    public void clearNonConnectedDevices(){
        for(int i=0; i<mBleDevices.size(); i++){
            if(!BleManager.getInstance().isConnected(mBleDevices.get(i))){
                mBleDevices.remove(i);
            }
        }
    }

    public void clear(){
        mBleDevices.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView device_name, device_id, device_connected;
        Button btn_connect, btn_disconnect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            device_name = itemView.findViewById(R.id.device_name);
            device_id = itemView.findViewById(R.id.device_id);
            device_connected = itemView.findViewById(R.id.device_connected);
            btn_connect = itemView.findViewById(R.id.btn_connect);
            btn_disconnect = itemView.findViewById(R.id.btn_disconnect);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                mListener.onDetail(mBleDevices.get(getAdapterPosition()));
            }
        }
    }

    public interface OnDeviceClickListener {

        void onConnect(BleDevice bleDevice);

        void onDisConnect(BleDevice bleDevice);

        void onDetail(BleDevice bleDevice);
    }

    private OnDeviceClickListener mListener;

    public void setOnDeviceClickListener(OnDeviceClickListener listener) {
        this.mListener = listener;
    }
}
