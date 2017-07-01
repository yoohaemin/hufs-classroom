package models.services

import java.util.UUID
import java.time.DayOfWeek
import javax.inject.Inject

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import models.daos.ClassroomDAO

import models._

class ClassroomServiceImpl @Inject()(classroomDAO: ClassroomDAO) extends ClassroomService {
  def add(classroom: Classroom): Future[Classroom] = classroomDAO.add(classroom)

  def find(dow: DayOfWeek, startTime: Int, endTime: Int, building: Option[Building]): Future[Seq[Classroom]] = building match {
    case Some(b) =>
      Future.traverse(startTime to endTime) { t => classroomDAO.getEmptyRooms(dow, t, b) } map { _.flatten }
    case None =>
      Future.traverse(startTime to endTime) { t => classroomDAO.getEmptyRooms(dow, t) } map { _.flatten }
  }
}