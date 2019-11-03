package com.github.admitrevskiy.githubauth.model.repo

import com.google.gson.*
import java.lang.reflect.Type

/**
 * Simple GSON deserializer to parse response with
 */
class GitHubRepoDeserializer : JsonDeserializer<GitHubRepo> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): GitHubRepo {
        val githubRepo = GitHubRepo()
        json.asJsonObject.apply {
            githubRepo.lastUpdateTime = getElement("updated_at")?.asString
            githubRepo.creationTime = getElement("created_at")?.asString
            githubRepo.description = getElement("description")?.asString
            githubRepo.htmlUrl = getElement("https_url")?.asString
            githubRepo.language = getElement("language")?.asString
            githubRepo.homepage = getElement("homepage")?.asString
            githubRepo.fork = getElement("fork")?.asBoolean
            githubRepo.name = getElement("name")?.asString
            githubRepo.url = getElement("url")?.asString
            githubRepo.forks = getElement("forks")?.asInt
            githubRepo.owner = getElement("owner")!!.asJsonObject.get("login").asString
            githubRepo.stars = getElement("stargazers_count")?.asInt
            githubRepo.issues = getElement("open_issues")?.asInt
            githubRepo.watchers = getElement("watchers")?.asInt
        }
        return githubRepo
    }
    
    private fun JsonObject.getElement(tag: String) : JsonElement? {
        this.get(tag)?.apply {
            return if (!isJsonNull) {
                this
            } else { null } 
        }
        return null
    }
}