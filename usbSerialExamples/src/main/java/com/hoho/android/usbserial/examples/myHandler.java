package com.hoho.android.usbserial.examples;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;

public class myHandler extends Handler {

    @SuppressLint("DefaultLocale")
    @Override
    public void handleMessage(@NonNull Message msg) {
        Passer passer= (Passer) msg.obj;
        byte[] data= passer.data;
        int id = byteToInt(data[0]);
        switch(id){
            case 32://wheel :
                int strategy=byteToInt(data[1]);
                    passer.handler.post(() -> passer.strategy.setText(String.format("%d",strategy)));
                if(data[2]!=0)  //motor on
                    passer.handler.post(() -> passer.motorOn.setBackgroundColor(Color.parseColor("#FF0000")));
                else            //motor off
                    passer.handler.post(() -> passer.motorOn.setBackgroundColor(Color.TRANSPARENT));
                if(data[3]!=0)  //purge on
                    passer.handler.post(() -> passer.purge.setBackgroundColor(Color.parseColor("#00FF00")));
                else            //purge off
                    passer.handler.post(() -> passer.purge.setBackgroundColor(Color.TRANSPARENT));
                if(data[4]!=0)  //powerMode on
                    passer.handler.post(() -> passer.SCVoltage.setBackgroundColor(Color.parseColor("#FF00FF")));
                else            //powerMode off
                    passer.handler.post(() -> passer.SCVoltage.setBackgroundColor(Color.TRANSPARENT));
                if(data[5]!=0)  //short on
                    passer.handler.post(() -> passer._short.setBackgroundColor(Color.parseColor("#FFFF00")));
                else            //short off
                    passer.handler.post(() -> passer._short.setBackgroundColor(Color.TRANSPARENT));
                break;
            case 16://service board: emergences
                for(int i=1;i<5;i++)
                    if(data[i]!=0)
                        passer.handler.post(() -> passer.emergences.setBackgroundColor(Color.parseColor("#FF0000")));
                break;
            case 17://service board: speed
                float speed=byteToFloat(data[4],data[3],data[2],data[1]);
                passer.handler.post(() -> passer.speed.setText(String.format("%f Km/h",speed)));
                break;
            case 18://service board: temperature
                float temperature=byteToFloat(data[4],data[3],data[2],data[1]);
                passer.handler.post(() -> passer.temperature.setText(String.format("%f",temperature)));
                break;
            case 19://service board: FCVoltage
                float FCVoltage=byteToFloat(data[4],data[3],data[2],data[1]);
                passer.handler.post(() -> passer.FCVoltage.setText(String.format("%f",FCVoltage)));
                break;
            case 20://service board: SCVoltage
                float SCVoltage=byteToFloat(data[4],data[3],data[2],data[1]);
                passer.handler.post(() -> passer.SCVoltage.setText(String.format("%f",SCVoltage)));
                break;
            case 48://actuation board: FCCurrent
                float FCCurrent=byteToFloat(data[4],data[3],data[2],data[1]);
                passer.handler.post(() -> passer.FCCurrent.setText(String.format("%f",FCCurrent)));
                break;
            default:
                break;
        }
    }

    public int byteToInt(byte... data) {
        int val = 0;
        for (byte datum : data) {
            val = val << 8;
            val = val | (datum & 0xFF);
        }
        return val;
    }
    public float byteToFloat(byte... data) {
        return ByteBuffer.wrap(data).getFloat();
    }
}
