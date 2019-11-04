package com.github.admitrevskiy.githubauth.view.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.admitrevskiy.githubauth.model.repo.GitHubRepo
import com.github.admitrevskiy.githubauth.R
import com.github.admitrevskiy.githubauth.extensions.view.setTextOrHide
import com.github.admitrevskiy.githubauth.extensions.view.inverseVisibility
import com.github.admitrevskiy.githubauth.view.helpers.RepositoryAdapter.ViewHolder

/**
 * Adapter for RecyclerView to show GitHubRepos list
 */
class RepositoryAdapter(private val context: Context, private val repos: List<GitHubRepo>) :

    RecyclerView.Adapter<ViewHolder>() {
    override fun getItemCount(): Int = repos.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        repos[position].apply {

            holder.name.text = name
            holder.summary.text =
                if (description != null) description else context.getString(R.string.no_description)

            holder.created.setTextOrHide(R.string.created, creationTime)
            holder.updated.setTextOrHide(R.string.updated, lastUpdateTime)
            holder.language.setTextOrHide(R.string.language, language)

            holder.forks.text = context.getString(R.string.forks, this.forks)
            holder.stars.text = context.getString(R.string.stars, this.stars)
            holder.issues.text = context.getString(R.string.opened_issues, this.issues)
            holder.watchers.text = context.getString(R.string.watchers, this.watchers)

            holder.info.visibility = View.GONE
            holder.wrapper.setOnClickListener { holder.info.inverseVisibility() }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val summary: TextView = itemView.findViewById(R.id.summary)
        val info: ViewGroup = itemView.findViewById(R.id.info_wrapper)
        val created: TextView = itemView.findViewById(R.id.created)
        val updated: TextView = itemView.findViewById(R.id.updated)
        val forks: TextView = itemView.findViewById(R.id.forks)
        val language: TextView = itemView.findViewById(R.id.language)
        val wrapper: ViewGroup = itemView.findViewById(R.id.wrapper)
        val stars: TextView = itemView.findViewById(R.id.stars)
        val issues: TextView = itemView.findViewById(R.id.issues)
        val watchers: TextView = itemView.findViewById(R.id.watchers)
    }
}