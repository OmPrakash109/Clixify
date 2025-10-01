### GitHub Guide for merging current branch to 'main' branch
Q) I wanna push latest progress to 'main' branch, should I push directly or merge with 'jwt-auth'(current branch), where I pushed the progress already.
- Ans:
  It's generally better to merge your 'jwt-auth' branch(current branch) into main rather than pushing directly to main.
  Here's the recommended workflow:

  - First, make sure all your changes are committed to jwt-auth:
    - git checkout jwt-auth
    - git status  # Make sure there are no uncommitted changes

  - Update your local main branch:
    - git checkout main
    - git pull origin main

  - Merge jwt-auth into main:
    - git merge jwt-auth

  - Resolve any merge conflicts if they occur (though there shouldn't be any if main hasn't changed)
  - Push the updated main branch:
    - git push origin main



# Complete Git Merge Guide

## Table of Contents
- [Merging to Main Branch](#merging-to-main-branch)
- [Merging Between Feature Branches](#merging-between-feature-branches)
- [Handling Merge Conflicts](#handling-merge-conflicts)
- [Common Scenarios](#common-scenarios)
- [Best Practices](#best-practices)
- [Troubleshooting](#troubleshooting)

## Merging to Main Branch

### When to Use
- When your feature is complete and ready for production
- When you want to update the main branch with your changes

### Steps to Merge to Main

1. **Commit all changes** to your feature branch:
   ```bash
   git checkout your-feature-branch
   git add .
   git commit -m "Your descriptive commit message"
   git push origin your-feature-branch
   ```

2. **Switch to main branch** and update it:
   ```bash
   git checkout main
   git pull origin main
   ```

3. **Merge your feature branch** into main:
   ```bash
   git merge your-feature-branch
   ```

4. **Resolve any conflicts** (see [Handling Merge Conflicts](#handling-merge-conflicts))

5. **Push the changes** to remote:
   ```bash
   git push origin main
   ```

## Merging Between Feature Branches

### When to Use
- When you need changes from one feature branch in another
- When you want to keep feature branches in sync

### Steps to Merge Between Branches

1. **Make sure your changes are committed** on the source branch:
   ```bash
   git checkout source-branch
   git status  # Check for uncommitted changes
   git push origin source-branch
   ```

2. **Switch to the target branch** and update it:
   ```bash
   git checkout target-branch
   git pull origin target-branch
   ```

3. **Merge the source branch**:
   ```bash
   git merge source-branch
   ```

4. **Resolve any conflicts** (see next section)

5. **Push the updated branch**:
   ```bash
   git push origin target-branch
   ```

## Handling Merge Conflicts

### Identifying Conflicts
After running `git merge`, you might see:
```
CONFLICT (content): Merge conflict in filename
Auto-merging filename
CONFLICT (content): Merge conflict in anotherfile
```

### Resolving Conflicts

1. **Open each conflicted file** in your code editor
2. **Look for conflict markers**:
   ```plaintext
   <<<<<<< HEAD
   Code from your current branch
   =======
   Code from the branch being merged
   >>>>>>> branch-name
   ```

3. **Resolve each conflict** by:
  - Deciding which changes to keep
  - Removing the conflict markers
  - Saving the file

4. **Mark files as resolved**:
   ```bash
   git add resolved-file.txt
   ```

5. **Complete the merge**:
   ```bash
   git commit -m "Resolved merge conflicts between source-branch and target-branch"
   ```

## Common Scenarios

### 1. Clean Merge (No Conflicts)
```bash
git checkout target-branch
git merge source-branch
git push origin target-branch
```

### 2. Aborting a Merge
```bash
git merge --abort
```

### 3. Viewing Merge History
```bash
git log --oneline --graph --all -n 10
```

### 4. Force Push (Use with Caution)
```bash
git push --force-with-lease origin branch-name
```

## Best Practices

1. **Before Merging**:
  - Make sure your working directory is clean
  - Pull the latest changes from both branches
  - Run tests before and after merging

2. **During Merging**:
  - Keep merge commits clear and descriptive
  - Use `--no-ff` for feature branch merges to preserve history:
    ```bash
    git merge --no-ff feature-branch
    ```

3. **After Merging**:
  - Test your application thoroughly
  - Delete merged branches that are no longer needed:
    ```bash
    git branch -d branch-name
    git push origin --delete branch-name
    ```

## Troubleshooting

### Common Issues

1. **Merge Conflict Resolution Failed**
  - If you need to start over:
    ```bash
    git merge --abort
    ```

2. **Accidental Push to Wrong Branch**
  - Use `git reflog` to find the previous state
  - Reset if needed:
    ```bash
    git reset --hard HEAD~1
    git push --force-with-lease origin branch-name
    ```

3. **Merge Left Unfinished**
  - If you see "You have unmerged paths":
    ```bash
    # Either complete the merge:
    git add .
    git commit -m "Resolve merge conflicts"
    
    # Or abort:
    git merge --abort
    ```

### Helpful Commands

- View changes between branches:
  ```bash
  git diff branch1..branch2
  ```

- See what will be merged:
  ```bash
  git diff --name-status branch1..branch2
  ```

- Check for unmerged files:
  ```bash
  git diff --name-only --diff-filter=U
  ```

---

This guide covers the most common Git merge scenarios. Remember to always pull the latest changes before merging and test your code after merging.
