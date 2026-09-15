package com.example.classschedule


import org.junit.Test
import org.junit.Assert.*
//import kotlin.test.assertEquals
//import kotlin.test.assertFalse
//import kotlin.test.assertTrue

class ScheduleViewModelTest {
    @Test
    fun testAddClassAddsToList() {
        val vm = ScheduleViewModel()
        vm.addClass("CS 1010")
        assertTrue(vm.classesPublic.value.contains("CS 1010"))
    }

    @Test
    fun testAddMultipleClasses() {
        val vm = ScheduleViewModel()
        vm.addClass("CS 1010")
        vm.addClass("CS 2010")
        vm.addClass("CS 3010")
        assertEquals(3, vm.classesPublic.value.size)
    }

    @Test
    fun testEditClassMarksDoneIfNewCourseMatches() {
        val vm = ScheduleViewModel()
        vm.addReq("CS 2020")
        vm.addClass("CS 1010")
        vm.editClass("CS 1010", "CS 2020")
        assertTrue(vm.reqPublic.value.contains("CS 2020 (DONE)"))
    }

    @Test
    fun testEditClassRemovesDoneIfNewCourseDoesNotMatch() {
        val vm = ScheduleViewModel()
        vm.addReq("CS 1010")
        vm.addClass("CS 1010")
        vm.editClass("CS 1010", "CS 2020")
        assertTrue(vm.reqPublic.value.contains("CS 1010"))
        assertFalse(vm.reqPublic.value.contains("CS 1010 (DONE)"))
    }

    @Test
    fun testSelectMajorPreservesClasses() {
        val vm = ScheduleViewModel()
        vm.addClass("CS 1010")
        vm.addClass("CS 2010")
        vm.selectMajor("Biology")
        assertTrue(vm.classesPublic.value.contains("CS 1010"))
        assertTrue(vm.classesPublic.value.contains("CS 2010"))
        assertEquals(2, vm.classesPublic.value.size)
    }

    @Test
    fun testCompleteWorkflow() {
        val vm = ScheduleViewModel()
        vm.selectMajor("Computer Science")
        assertEquals(4, vm.reqPublic.value.size)
        vm.addClass("CS 1010")
        vm.addClass("CS 2010")
        assertEquals(2, vm.classesPublic.value.size)
        vm.editClass("CS 1010", "CS 3010")
        assertTrue(vm.classesPublic.value.contains("CS 3010"))
        assertTrue(vm.reqPublic.value.contains("CS 3010 (DONE)"))
        vm.removeClass("CS 2010")
        assertEquals(1, vm.classesPublic.value.size)
    }

}