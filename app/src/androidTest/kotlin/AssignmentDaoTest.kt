import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wcupa.assignmentNotifier.data.Assignment
import com.wcupa.assignmentNotifier.data.AssignmentDao
import com.wcupa.assignmentNotifier.data.AssignmentDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.Date

@RunWith(AndroidJUnit4::class)
class AssignmentDaoTest {
    private lateinit var assignmentDao: AssignmentDao
    private lateinit var assignmentDatabase: AssignmentDatabase
    
   private val currentDate =  Date()

    private var assignment1 = Assignment(1, "Assignment 1", currentDate, "Android App Development",false)
    private var assignment2 = Assignment(2, "Final Exam", currentDate, "Statistics",false)


    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        assignmentDatabase = Room.inMemoryDatabaseBuilder(context, AssignmentDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        assignmentDao = assignmentDatabase.assignmentDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        assignmentDatabase.close()
    }

    private suspend fun addOneAssignmentToDb() {
        assignmentDao.insert(assignment1)
    }

    private suspend fun addTwoAssignmentsToDb() {
        assignmentDao.insert(assignment1)
        assignmentDao.insert(assignment2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsAssignmentIntoDB() = runBlocking {
        addOneAssignmentToDb()
        val allAssignments = assignmentDao.getAllAssignments().first()
        assertEquals(allAssignments[0], assignment1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllAssignments_returnsAllAssignmentsFromDB() = runBlocking {
        addTwoAssignmentsToDb()
        val allAssignments = assignmentDao.getAllAssignments().first()
        assertEquals(allAssignments[0], assignment1)
        assertEquals(allAssignments[1], assignment2)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateAssignments_updatesAssignmentsInDB() = runBlocking {
        addTwoAssignmentsToDb()
        assignmentDao.update(Assignment(1, "Assignment 1", currentDate, "Android App Development",false))
        assignmentDao.update(Assignment(2, "Final Exam", currentDate, "Statistics"))

        val allAssignments = assignmentDao.getAllAssignments().first()
        assertEquals(allAssignments[0],  Assignment(1, "Assignment 1", currentDate, "Android App Development",false))
        assertEquals(allAssignments[1], Assignment(2, "Final Exam", currentDate, "Statistics", false))
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteAssignments_deletesAllAssignmentsFromDB() = runBlocking {
        addTwoAssignmentsToDb()
        assignmentDao.delete(assignment1)
        assignmentDao.delete(assignment2)
        val allAssignments = assignmentDao.getAllAssignments().first()
        assertTrue(allAssignments.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAssignment_returnsAssignmentFromDB() = runBlocking {
        addOneAssignmentToDb()
        val assignment = assignmentDao.getAssignment(1)
        assertEquals(assignment.first(), assignment1)
    }
}