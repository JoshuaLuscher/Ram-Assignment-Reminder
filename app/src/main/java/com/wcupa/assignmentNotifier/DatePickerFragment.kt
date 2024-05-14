package com.wcupa.assignmentNotifier


import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.wcupa.assignmentNotifier.ui.assignment.AssignmentEntryDestination.setContentView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
//Fragment that controls the date-picker, the calendar for inputting due dates
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("MMM. dd, yyyy", Locale.US)

    private var view: View? = null

    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.datepicker, container, false)
        // Find the table using the view
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        // Use the current date as the default date in the picker.
        setContentView(R.layout.datepicker)

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it.
        return DatePickerDialog(activity, this, year, month, day)

    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        calendar.set(year, month, day)
        returnFormattedDate(calendar.timeInMillis)
    }

    private fun returnFormattedDate(timestamp: Long): String? {
        return formatter.format(timestamp)

    }
}