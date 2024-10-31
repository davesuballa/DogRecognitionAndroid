package com.example.buddy
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.text.SimpleDateFormat
import java.util.Locale

class EventDecorator(
    private val calendar: calendar,
    private val calendarView: MaterialCalendarView,
    private val highlightColor: Int
) : DayViewDecorator {

    private val dotRadius = 10f
    private val drawable: Drawable?

    override fun shouldDecorate(day: CalendarDay): Boolean {
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(day.date)
        return calendar.eventsMap.containsKey(formattedDate)
    }

    override fun decorate(view: DayViewFacade) {
        drawable?.setTint(highlightColor)
        view.setSelectionDrawable(drawable!!)
    }

    init {
        drawable = ContextCompat.getDrawable(calendarView.context, R.drawable.check_off_background)?.mutate()
    }
}