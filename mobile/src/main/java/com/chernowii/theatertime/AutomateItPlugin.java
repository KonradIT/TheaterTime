package com.chernowii.theatertime;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.smarterapps.automateitplugin.sdk.PluginDataFieldCollection;
import com.smarterapps.automateitplugin.sdk.PluginValidationResult;
import com.smarterapps.automateitplugin.sdk.RequiredApp;
import com.smarterapps.automateitplugin.sdk.fields.PluginDataFieldBoolean;
import com.smarterapps.automateitplugin.sdk.fields.PluginDataFieldString;

import java.util.List;

/**
 * Created by Konrad Iturbe on 02/19/16.
 */
public class AutomateItPlugin extends com.smarterapps.automateitplugin.sdk.PluginAction{

    private static final int FIELD_ID_DURATION_LONG = 2;

    @Override
    public String getActionTitle(Context context)
    {
        return context.getString(R.string.action1_title);
    }

    @Override
    public String getActionDescription(Context context)
    {
        return context.getString(R.string.action1_default_description);
    }

    @Override
    public String getActionDescription(Context context, PluginDataFieldCollection data)
    {
        if (null != data)
        {

            boolean durationLong = (Boolean) data.getFieldById(FIELD_ID_DURATION_LONG).getValue();

            int toastDurationStringId = durationLong ? R.string.duration_long: R.string.duration_short;


        }

        return getActionDescription(context);
    }

    @Override
    public int getActionIconResourceId()
    {
        return R.mipmap.ic_launcher;
    }

    @Override
    public int getActionSmallIconResourceId()
    {
        return R.mipmap.ic_launcher;
    }

    @Override
    public PluginDataFieldCollection getActionFields(Context context)
    {
        PluginDataFieldCollection retval = new PluginDataFieldCollection();

        retval.add(new PluginDataFieldBoolean(
                FIELD_ID_DURATION_LONG,
                context.getString(R.string.action1_field_duration_title),
                context.getString(R.string.action1_field_duration_description),
                true));



        return retval;
    }

    @Override
    public void doAction(Context context, PluginDataFieldCollection data)
    {
        boolean durationLong = (Boolean) data.getFieldById(FIELD_ID_DURATION_LONG).getValue();
        int toastDuration = durationLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Intent intent = new Intent(String.valueOf(toastDuration));
        Bundle extras = new Bundle();
        extras.putString("send_data", "test");
        intent.putExtras(extras);
        context.sendBroadcast(intent);

    }

    @Override
    public PluginValidationResult validateData(Context context, PluginDataFieldCollection data)
    {
        // Validate action1
        String toastMsg = (String) data.getFieldById(FIELD_ID_DURATION_LONG).getValue();

        if (null == toastMsg || toastMsg.trim().length() == 0)
        {
            return new PluginValidationResult(false, "control can't be empty");
        }

        return PluginValidationResult.getValidResult();
    }

    @Override
    public List<RequiredApp> getRequiredApps()
    {
        return null;
    }
}
