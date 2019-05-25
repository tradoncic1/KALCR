package com.example.hciapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class EventListAdapterOwner extends BaseAdapter {
    private Context context;
    private List<Event> eventList;

    public EventListAdapterOwner(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        convertView = layoutInflater.inflate(R.layout.event_list_item_owner, parent, false);

        final Event event = eventList.get(position);

        TextView eventTitle = convertView.findViewById(R.id.artistName);
        final TextView eventLocation = convertView.findViewById(R.id.eventLocationInput);
        TextView eventDateTime = convertView.findViewById(R.id.eventDateTime);
        TextView eventPrice = convertView.findViewById(R.id.eventPrice);
        TextView eventType = convertView.findViewById(R.id.eventType);
        ImageView eventImage = convertView.findViewById(R.id.artistImage);

        Button deleteBtn = convertView.findViewById(R.id.deleteBtn);

        eventTitle.setText(event.getTitle());
        eventLocation.setText(event.getLocation());
        eventDateTime.setText(event.getDateTime());
        eventPrice.setText(event.getPrice()+"$");
        eventType.setText(event.getType());

        Bitmap bitmap = BitmapFactory.decodeByteArray(event.getEventImage(), 0, event.getEventImage().length);
        eventImage.setImageBitmap(bitmap);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyDatabase.getDatabase(context).eventsDAO().deleteEvent(event);

                eventList.clear();
                eventList.addAll(MyDatabase.getDatabase(context).eventsDAO().getAllEvents());
                notifyDataSetChanged();
                //((Activity)context.getApplicationContext()).finish();
                //context.getApplicationContext().startActivity(new Intent(context, VenueOwnerMainActivity.class));
                //((Activity)context.getApplicationContext()).overridePendingTransition(0, 0);
            }
        });

        return convertView;
    }
}
