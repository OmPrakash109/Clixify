# Git Commit Recovery Guide

> **What this document helps with**: Recovering commits that were lost due to operations like `git reset --hard` or accidental branch deletion.

This guide helps you recover lost commits using Git's reflog, which keeps a history of all reference updates (like branch movements) in your local repository.

## Quick Reference: Recovery Steps

### Option 1: Reset current branch (what we actually did)
```bash
# 1. Check reflog to find the lost commit
git reflog
# Look for something like: 269af9f (feature/url-shortening) HEAD@{1}

# 2. Reset current branch to that commit
git reset --hard 269af9f

# 3. Verify the changes are back
git status
git log --oneline
```

### Option 2: Create a new branch (alternative approach)
```bash
# 1. Check reflog to find the lost commit
git reflog

# 2. Create and switch to a new branch at that commit
git checkout -b recovered-branch 269af9f

# 3. Verify the changes are there
git status
git log --oneline
```

> **Note**: The first option modifies your current branch, while the second creates a new branch without affecting the original one.
git reset --hard 269af9f

---

## Problem
Let's say you were working on the `jwt-auth` branch and accidentally ran:
```bash
git reset --hard HEAD~1
```
This command removed your most recent commit from the current branch, potentially causing loss of uncommitted changes and recent commits.

## Solution: Recovering Lost Commits Using Git Reflog

### Step 1: Check your current branch and status
First, verify which branch you're on and what changes were lost:
```bash
# See which branch you're currently on
git branch

# Check the status of your working directory
git status

# View the last few commits to see what was lost
git log -n 5 --oneline
```

> **Note**: If you just ran the reset command, you're still on the same branch (`jwt-auth` in our example), but you've moved its pointer back one commit.

### Step 2: View your Git reflog to find the lost commit
The reflog keeps track of all reference updates (like branch movements) in your local repository:

```bash
# Show the reference log
git reflog

# For more detailed output with dates and commit messages:
git log -g
```

Look for entries that show your branch before the reset. You'll see something like:
```
269af9f (HEAD -> jwt-auth, origin/jwt-auth) HEAD@{1}: commit: feat(url): implemented URL shortening
```

> **Important**: The `HEAD@{1}` indicates this was the state before your last operation. The number in `HEAD@{n}` increases as you perform more operations.

### Step 3: Identify the commit hash before the reset
In the reflog output, find the commit that represents the state you want to return to. Look for:
- The commit message that matches your lost work
- The branch name you were working on (e.g., `jwt-auth`)
- The `HEAD@{n}` reference showing the state before the reset

Example of what to look for:
```
269af9f (HEAD -> jwt-auth, origin/jwt-auth) HEAD@{1}: commit: feat(url): implemented URL shortening with unique code generation and secure endpoints
d5a5199 HEAD@{2}: checkout: moving from main to jwt-auth
```

In this example, `269af9f` is the commit we want to recover, and it was the state of `jwt-auth` branch before the reset.

### Step 4: Reset to the specific commit
Once you've identified the commit hash (`269af9f` in our example), you have two options:

#### Option A: If you want to restore the branch to its previous state
```bash
# Make sure you're on the correct branch (jwt-auth in our case)
git checkout jwt-auth

# Reset the branch to the specific commit

# Force push to update the remote branch (only if you've already pushed before)
git push --force-with-lease origin jwt-auth
```

#### Option B: If you want to create a new branch from the recovered state
```bash
# Create and switch to a new branch from the recovered commit
git checkout -b recovered-url-feature 269af9f
```

> **Warning**: `--force` and `--hard` are destructive operations. Be absolutely sure you're on the right branch before running these commands.

### Step 5: Verify the recovery
After resetting or creating a new branch, verify your changes are back:

```bash
# Check your current branch
git branch

# View the commit history
git log --oneline -n 5

# Check the status of your working directory
git status

# If you created a new branch, push it to remote
git push -u origin recovered-url-feature
```

If everything looks correct, your changes should be restored. If you're working with others, inform them about the force push if you did one.

## Alternative Approach: Create a New Branch from the Lost Commit

If you're not comfortable with resetting your current branch, you can create a new branch from the lost commit without affecting your current branch:

```bash
# From any branch, create and switch to a new branch at the lost commit
git checkout -b recovered-work 269af9f

# Push the new branch to remote
git push -u origin recovered-work
```

This approach is safer because:
1. It doesn't modify your existing branches
2. You can compare the branches before deciding which changes to keep
3. It preserves the original state in case you need to reference it later

### Step 4: Create a new branch from the lost commit
```bash
git branch feature/url-shortening 269af9f
```

### Step 5: Switch to the recovered branch
```bash
git checkout feature/url-shortening
```

### Step 6: Verify your changes are restored
```bash
git log --oneline
git status
```

## Best Practices to Avoid This in Future
1. **Always create a backup branch** before making major changes:
   ```bash
   git checkout -b backup-branch-name
   ```

2. **Stash changes** if you need to switch branches:
   ```bash
   git stash
   git checkout other-branch
   git stash pop
   ```

3. **Use `git reset --soft`** instead of `--hard` when possible, as it preserves changes in your working directory.

4. **Push your branches** regularly to remote:
   ```bash
   git push -u origin branch-name
   ```

## Additional Recovery Options
- If you didn't create a new branch, you can reset directly to the commit:
  ```bash
  git reset --hard 269af9f
  ```
  
- To recover uncommitted changes that were lost:
  ```bash
  git fsck --lost-found
  ```

Remember: `git reflog` is local to your machine and only contains actions from your local repository. It won't help if you delete your local repository or if the changes were never committed.
