package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.CampaignReport
import com.example.data.model.UserProfile
import com.example.util.OperationType
import com.example.util.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class CampaignReportRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google to access reports.")
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
        val payload = mapOf(
            "userId" to uid,
            "email" to profile.email,
            "displayName" to profile.displayName,
            "photoUrl" to profile.photoUrl,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        ).filterValues { it != null }

        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            throw e
        }
    }

    fun observeUserReports(): Flow<List<CampaignReport>> = flow {
        val uid = requireUserId()
        val path = "users/$uid/reports"
        val query = db.collection("users").document(uid).collection("reports")
            .orderBy("createdAt", Query.Direction.DESCENDING)

        query.snapshots()
            .map { snapshot -> snapshot.toObjects(CampaignReport::class.java) }
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                val exceptionToLog = firestoreError ?: (error as? Exception)
                if (exceptionToLog != null) {
                    handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                }
                throw error
            }
            .collect { emit(it) }
    }

    suspend fun saveReport(report: CampaignReport): String {
        val uid = requireUserId()
        val collection = db.collection("users").document(uid).collection("reports")
        val docRef = if (report.id.isNotBlank()) collection.document(report.id) else collection.document()

        val payload = mapOf(
            "userId" to uid,
            "theme" to report.theme,
            "village" to report.village,
            "tehsil" to report.tehsil,
            "district" to report.district,
            "state" to report.state,
            "claim" to report.claim,
            "groundReality" to report.groundReality,
            "tone" to report.tone,
            "visualProofSummary" to report.visualProofSummary,
            "suggestedHashtags" to report.suggestedHashtags,
            "alternativeTones" to report.alternativeTones,
            "twitterHindi" to report.twitterHindi,
            "twitterEnglish" to report.twitterEnglish,
            "instagramHindi" to report.instagramHindi,
            "instagramEnglish" to report.instagramEnglish,
            "linkedInHindi" to report.linkedInHindi,
            "linkedInEnglish" to report.linkedInEnglish,
            "facebookHindi" to report.facebookHindi,
            "facebookEnglish" to report.facebookEnglish,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        ).filterValues { it != null }

        return try {
            docRef.set(payload).await()
            docRef.id
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            throw e
        }
    }

    suspend fun deleteReport(reportId: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("reports").document(reportId)
        try {
            docRef.delete().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            throw e
        }
    }
}
