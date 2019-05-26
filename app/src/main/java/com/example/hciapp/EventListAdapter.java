package com.example.hciapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<Event> eventList;
    private List<Event> eventListFull;

    public EventListAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
        eventListFull = new ArrayList<>(eventList);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        convertView = layoutInflater.inflate(R.layout.event_list_item, parent, false);

        Event event = eventList.get(position);

        TextView eventTitle = convertView.findViewById(R.id.eventName);
        TextView eventLocation = convertView.findViewById(R.id.eventLocationInput);
        TextView eventDateTime = convertView.findViewById(R.id.eventDateTime);
        TextView eventPrice = convertView.findViewById(R.id.eventPrice);
        TextView eventType = convertView.findViewById(R.id.eventType);
        ImageView eventImage = convertView.findViewById(R.id.artistImage);

        eventTitle.setText(event.getTitle());
        eventLocation.setText(event.getLocation());
        eventDateTime.setText(event.getDateTime());
        eventPrice.setText(event.getPrice()+"$");
        eventType.setText(event.getType());

        Bitmap bitmap = BitmapFactory.decodeByteArray(event.getEventImage(), 0, event.getEventImage().length);
        eventImage.setImageBitmap(bitmap);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return filterResults;
    }

    private Filter filterResults = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Event> filteredList = MyDatabase.getDatabase(context.getApplicationContext()).eventsDAO().getAllEvents();

            String filterPattern = constraint.toString().toLowerCase();
            String[] filters = filterPattern.split("/");

            if (constraint.toString().trim().isEmpty()) {
                filteredList.clear();
                filteredList.addAll(MyDatabase.getDatabase(context.getApplicationContext()).eventsDAO().getAllEvents());
            } else {

                for (Event event: eventListFull) {
                    if (event.getLocation().equalsIgnoreCase(filters[0]) && filters[1].equalsIgnoreCase("empty")) {
                        filteredList.clear();
                        filteredList.add(event);
                    } else if (event.getType().equalsIgnoreCase(filters[1]) && filters[0].equalsIgnoreCase("empty")) {
                        filteredList.clear();
                        filteredList.add(event);
                    } else if (event.getLocation().equalsIgnoreCase(filters[0]) && event.getType().equalsIgnoreCase(filters[1])) {
                        filteredList.clear();
                        filteredList.add(event);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                eventList.clear();
                eventList.addAll((List) results.values);

            } else {
                eventList.clear();
                eventList.addAll(MyDatabase.getDatabase(context).eventsDAO().getAllEvents());
            }

            notifyDataSetChanged();
        }
    };
}
