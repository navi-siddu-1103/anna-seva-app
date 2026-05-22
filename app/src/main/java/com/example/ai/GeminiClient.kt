package com.example.ai

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

object GeminiClient {

    private const val TAG = "GeminiClient"
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    /**
     * Checks if the API key is set and is not a placeholder
     */
    fun isApiKeyAvailable(): Boolean {
        val apiKey = BuildConfig.GEMINI_API_KEY
        return !apiKey.isNullOrEmpty() && 
               apiKey != "MY_GEMINI_API_KEY" && 
               apiKey != "YOUR_GEMINI_API_KEY" && 
               apiKey != "key_placeholder"
    }

    /**
     * Calls Gemini API to get nutritional and personalized grocery suggestions
     */
    suspend fun getRationSuggestions(
        purchaseHistory: String,
        familySize: Int,
        preferences: String,
        regionalHabits: String
    ): String = withContext(Dispatchers.IO) {
        if (!isApiKeyAvailable()) {
            Log.w(TAG, "Gemini API key is not configured, using smart local rule-based advice engine.")
            return@withContext getLocalRuleBasedSuggestions(purchaseHistory, familySize, preferences, regionalHabits)
        }

        val apiKey = BuildConfig.GEMINI_API_KEY
        val prompt = """
            You are "Anna Seva AI", a helpful nutritionist and smart PDS Panning assistant. 
            Analyze the following family information and provide personalized, highly encouraging grocery, health, and menu suggestions. Set a warm, professional, respectful tone supporting underprivileged citizens.
            
            FAMILY INFO:
            - Purchase History: $purchaseHistory
            - Family Size: $familySize members
            - Special Preferences/Health: $preferences
            - Regional Food Habits: $regionalHabits
            - Basic Monthly Entitlement: Rice (5 kg/member), Wheat (5 kg/member), Sugar (2 kg/family)
            
            Please suggest:
            1. **Optimal Utilization**: How to combine Rice, Wheat, Sugar with other affordable elements (oil, flour, lentils) for maximum nutrition.
            2. **Healthy Daily Recipes**: Suggest 2 simple, healthy regional recipes they can cook using PDS ingredients.
            3. **Nutritional Boosting**: Recommend seasonal vegetables or budget-friendly nutritional supplements (like fortified salt, groundnuts, green leafy vegetables) suitable for a $familySize-person household.
            4. **Storage Tip**: A helpful hygiene tip to store dry rations safely and prevent spoilage.

            Please reply in cleanly formatted Markdown with clear section headers, nice bullet points, and no robotic opening/taglines.
        """.trimIndent()

        try {
            val jsonRequest = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            val partObj = JSONObject().apply {
                                put("text", prompt)
                            }
                            put(partObj)
                        }
                        put("parts", partsArray)
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonRequest.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errCode = response.code
                    val errMsg = response.body?.string() ?: "No error body"
                    Log.e(TAG, "API Error $errCode: $errMsg")
                    return@withContext "*(Serving smart local advice due to remote service error code $errCode)*\n\n" +
                            getLocalRuleBasedSuggestions(purchaseHistory, familySize, preferences, regionalHabits)
                }

                val responseBodyStr = response.body?.string() ?: throw IOException("Empty response")
                val jsonResponse = JSONObject(responseBodyStr)
                Log.d(TAG, "Response size: ${responseBodyStr.length}")

                val textResponse = jsonResponse
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")

                return@withContext textResponse
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed calling Gemini API", e)
            return@withContext "*(Serving smart local advice due to connection exception: ${e.localizedMessage})*\n\n" +
                    getLocalRuleBasedSuggestions(purchaseHistory, familySize, preferences, regionalHabits)
        }
    }

    /**
     * High-quality Indian dietetics rule-based fallback advice based on PDS/regional settings.
     */
    private fun getLocalRuleBasedSuggestions(
        purchaseHistory: String,
        familySize: Int,
        preferences: String,
        regionalHabits: String
    ): String {
        val totalRice = familySize * 5
        val totalWheat = familySize * 5
        val totalSugar = 2

        val isRicePref = regionalHabits.contains("South", ignoreCase = true) || regionalHabits.contains("Rice", ignoreCase = true)
        val isRotiPref = regionalHabits.contains("North", ignoreCase = true) || regionalHabits.contains("Wheat", ignoreCase = true) || regionalHabits.contains("Roti", ignoreCase = true)
        
        return """
            ### 🍚 Anna Seva AI - Personalized Advisory (Offline Mode)
            
            *Your API Key is currently set to placeholder mode, so we are calculating localized wellness planning offline using Indian PDS Nutritional Guidelines.*

            #### 1. 🌾 Optimal Utilization for $familySize Members
            With your monthly allocations of **$totalRice kg Rice**, **$totalWheat kg Wheat**, and **$totalSugar kg Sugar**:
            ${if (isRicePref) {
                "- **Rice Staple Balance**: Since your region prefers rice, pair it with local dal/lentils to create a complete amino-acid protein profile. Minimize sugar intake by preparing brown-rice options or fermented food like Idlis, which increases bioavailability of B-Vitamins."
            } else if (isRotiPref) {
                "- **Wheat Flour Booster**: Since you prefer wheat-based options, grind wheat with locally available Bengal Gram (Chana) in an 4:1 ratio. This fortifies your rotis with exceptional protein and fiber, stabilizing blood sugar peaks."
            } else {
                "- **Balanced Split**: Distribute your meals with wheat rotis for lunch (complex carbs for sustained daytime energy) and steamed rice with seasonal vegetables for dinner (easy digestion for restful sleep)."
            }}
            - **Healthy Fats Integration**: Pair your grains with fortified mustard or sunflower options from the depot. Standardize to 30g per adult member daily.

            #### 2. 🍲 Highly Nutritious Regional Recipes
            - **Fortified Khichdi (Protein & Energy Boost)**:
              - *Ingredients*: 1 cup ration rice, 1/2 cup split Moong Dal, seasonal carrots/green beans, 1 tsp ghee/oil, turmeric.
              - *Nutrition*: High fiber, highly digestible proteins, essential minerals. Excellent for children and elderly.
            - **Missi Roti (Slow-burning Wheat Combo)**:
              - *Ingredients*: 2 cups ration wheat flour, 1/2 cup chickpea flour (Besan), minced onions, carom seeds (Ajwain), local greens.
              - *Nutrition*: Low glycemic index, pack with zinc, iron, and slow-release healthy carbohydrates.

            #### 3. 🥦 Budget-Friendly Nutritional Additions
            To enrich your standard $totalRice kg grain profile, prioritize adding these local items:
            - **Seasonal Green Leaves (Spinach, Amaranth, Mint)**: Extremely cheap source of Iron, Vitamin A, and Calcium to fight anemia.
            - **Roasted Chickpeas & Peanuts**: Highly affordable protein snacks. Great for school-going kids to meet daily growth targets.
            - **Sprouted Pulses**: Enhances Vitamin C and digestive enzymes, which help absorb iron from wheat.

            #### 4. 🪚 Storage & Hygiene Guidelines
            To preserve your $totalRice kg Rice and $totalWheat kg Wheat safely for 30 days:
            - **Dry Neems**: Place dried neem leaves or cloves at the bottom of standard grain tins. It acts as an organic, chemical-free repellent for weevils.
            - **Airtight Standard**: Ensure grains are fully sun-dried for 4 hours before sealing in airtight steel drums to eliminate residual moisture.

            ---
            *💡 Tip: To unlock the dynamic cloud-based Gemini conversational engine, paste your active Gemini API key in the AI Studio Secrets panel.*
        """.trimIndent()
    }
}
