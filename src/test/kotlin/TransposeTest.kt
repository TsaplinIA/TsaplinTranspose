import org.junit.Assert.*
import org.junit.Test
import java.io.File

class TransposeTest {

    fun assertFile(ref: String, comp:String): Boolean {
        val refFileText = File(ref).readText()
        val compFileText = File(comp).readText()
        return refFileText == compFileText
    }


    @Test
    fun readCom() {
        val temp = readCom("transpose [-a 3] [-t] [-r] [-o ofile] [file]")
        assertEquals("3", temp["-a"])
        assertEquals("true", temp["-t"])
        assertEquals("true", temp["-r"])
        assertEquals("ofile", temp["-o"])
        assertEquals("file", temp["file"])
    }
    @Test
    fun testResults() {
        Transpose("src\\test\\resources\\in.txt", "src\\test\\resources\\test1.txt", 3, true, true)
        Transpose("src\\test\\resources\\in.txt", "src\\test\\resources\\test2.txt", 2, true, true)
        Transpose("src\\test\\resources\\in.txt", "src\\test\\resources\\test3.txt", 3, false, false)
        Transpose("src\\test\\resources\\in.txt", "src\\test\\resources\\test4.txt", 3, true, false)

        assertTrue(assertFile("src\\test\\resources\\ref1.txt", "src\\test\\resources\\test1.txt"))
        assertTrue(assertFile("src\\test\\resources\\ref2.txt", "src\\test\\resources\\test2.txt"))
        assertTrue(assertFile("src\\test\\resources\\ref3.txt", "src\\test\\resources\\test3.txt"))
        assertTrue(assertFile("src\\test\\resources\\ref4.txt", "src\\test\\resources\\test4.txt"))
    }

    @Test
    fun testFlags() {
        val temp1 = Transpose("src\\test\\resources\\in.txt", "src\\test\\resources\\test1.txt", 3, true, true)
        assertTrue(temp1.r)
        assertTrue(temp1.t)
        assertEquals(3, temp1.a)
        assertEquals("src\\test\\resources\\test1.txt", temp1.ofile)
        assertEquals("src\\test\\resources\\in.txt", temp1.file)
    }

}