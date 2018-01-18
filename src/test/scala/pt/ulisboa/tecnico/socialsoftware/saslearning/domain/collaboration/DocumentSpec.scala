package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import java.net.URI
import javax.mail.internet.InternetAddress

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString
import org.scalatest.{EitherValues, Matchers, WordSpec}
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.User

import scala.collection.immutable.Seq

class DocumentSpec extends WordSpec
  with Matchers
  with EitherValues {

  private val user = User(None, username = "jdoe", email = new InternetAddress("john.doe@example.org"), displayName = "John Doe")

  private val defaultUri = URI.create("http://example.org")
  private val defaultTitle: NonEmptyString = Refined.unsafeApply("The history of Art")
  private val defaultContent: NonEmptyString = Refined.unsafeApply("Content goes here")

  private val defaultDocument = Document(None, defaultTitle, defaultUri, defaultContent, user, None)

  private val question = Question("What is art?", user)
  private val documentWithQuestion = defaultDocument.copy(thread = Some(Thread(Refined.unsafeApply(Seq(question)))))

  private def assertRight(expected: Document, actual: Either[String, Document]) = {
    actual should be('right)
    actual.right.value should be(expected)
  }

  "A document" should {
    "have a title" when {
      "is empty" in {
        val document = Document.fromUnsafe(None, "", defaultUri, defaultContent, user, None)
        document should be ('left)
      }
      "is not empty" in {
        val document = Document.fromUnsafe(None, "The history of Art", defaultUri, defaultContent, user, None)
        assertRight(expected = defaultDocument, actual = document)
      }
    }
    "have content" when {
      "is empty" in {
        val document = Document.fromUnsafe(None, defaultTitle, defaultUri, "", user, None)
        document should be ('left)
      }
      "is not empty" in {
        val document = Document.fromUnsafe(None, defaultTitle, defaultUri, "Content goes here", user, None)
        assertRight(expected = defaultDocument, actual = document)
      }
    }
  }

  "Posting a comment" should {
    "create a thread when it doesn't exist" in {
      val actual = defaultDocument.postComment(question)

      assertRight(expected = documentWithQuestion, actual)
    }
    "add it to the thread" in {
      val answer = Answer("The expression of human creative skills", user)
      val expected = defaultDocument.copy(thread = Some(Thread(Refined.unsafeApply(Seq(question, answer)))))

      val actual = documentWithQuestion.postComment(answer)

      assertRight(expected, actual)
    }
  }

  "Deleting a comment" should {
    "remove it from the thread" in {
      val answer = Answer("The expression of human creative skills", user)
      val documentWithTwoPosts = defaultDocument.copy(thread = Some(Thread(Refined.unsafeApply(Seq(question, answer)))))

      val actual = documentWithTwoPosts.deleteComment(answer)

      assert(documentWithQuestion == actual)
    }
    "delete the thread" in {
      val actual = documentWithQuestion.deleteComment(question)
      assert(defaultDocument == actual)
    }
  }

}