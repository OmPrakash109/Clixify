## Common Git Commands Cheat Sheet

### Basic Commands
- `git init` - Initialize a new Git repository
- `git clone <url>` - Clone a repository from URL
- `git status` - Show the working tree status
- `git add <file>` - Add file contents to the staging area
- `git commit -m "message"` - Commit changes with a message
- `git push` - Push changes to remote repository
- `git pull` - Fetch from and integrate with another repository

### Branching
- `git branch` - List all local branches
- `git branch <name>` - Create a new branch
- `git checkout <branch>` - Switch to a branch
- `git checkout -b <branch>` - Create and switch to new branch
- `git merge <branch>` - Merge a branch into current branch
- `git branch -d <branch>` - Delete a branch

### Undoing Changes
- `git restore <file>` - Discard changes in working directory
- `git restore --staged <file>` - Unstage a file
- `git reset --soft HEAD~1` - Undo last commit, keep changes staged
- `git reset --hard HEAD~1` - Completely remove last commit and changes
- `git revert <commit>` - Create a new commit that undoes a previous commit

### Remote Repositories
- `git remote -v` - List all remote repositories
- `git remote add <name> <url>` - Add a new remote
- `git fetch <remote>` - Download objects from remote
- `git push -u origin <branch>` - Set upstream branch

### Stashing
- `git stash` - Stash changes in working directory
- `git stash list` - List all stashes
- `git stash apply` - Apply most recent stash
- `git stash pop` - Apply and remove most recent stash
- `git stash drop` - Delete most recent stash

### Viewing History
- `git log` - Show commit logs
- `git log --oneline` - Compact commit logs
- `git log --graph` - Show commit graph
- `git show <commit>` - Show commit details
- `git diff` - Show changes between commits, commit and working tree, etc.

### Tagging
- `git tag` - List all tags
- `git tag <tagname>` - Create lightweight tag
- `git tag -a <tagname> -m "message"` - Create annotated tag
- `git push origin <tagname>` - Push tag to remote

### Configuration
- `git config --global user.name "Name"` - Set global username
- `git config --global user.email "email@example.com"` - Set global email
- `git config --list` - List all configurations



## Here are different types of commit messages commonly used in companies, following best practices:

### 1. Conventional Commits
   <type>[optional scope]: <description>

[optional body]

[optional footer(s)]

Examples:

    feat(auth): add JWT authentication
    fix(api): resolve null pointer in user registration
    docs(readme): update installation instructions
    refactor(service): optimize database queries
    test(security): add test cases for password reset

### 2. Feature/Component Based

    [COMPONENT] Brief description of changes

- Detail 1
- Detail 2
  - Fixes #issue-number
    Examples:

        [AUTH] Implement JWT token refresh

- Add refresh token endpoint
- Update token validation logic
- Fixes #123

### 3. JIRA/Ticket Based
   
    [PROJ-123] Brief description of changes

More detailed explanation if needed

### 4. Simple and Direct

   Add user registration endpoint
   Fix login validation bug
   Update API documentation
   Refactor authentication service


### 5. Detailed Technical

   [IMPACT] High/Medium/Low
   [COMPONENT] auth-service


## Description of changes:

- What was changed
- Why it was changed
- Any side effects

### Testing:

- [ ] Unit tests
- [ ] Integration tests
- [ ] Manual testing

### 6. For Bug Fixes

    Fix: Brief description of the issue

- Root cause analysis
- Solution implemented
- Testing done

Fixes #issue-number

### 7. For Hotfixes

    HOTFIX: Critical authentication bypass

- Fix security vulnerability in JWT validation
- Add additional input sanitization
- Update security tests

    Security: HIGH

### 8. For Documentation Updates

    docs: Update API documentation

- Add endpoint documentation
- Update request/response examples
- Fix typo in README

## Best Practices:

  - Keep the first line under 50 characters
  - Use the imperative mood ("Add" not "Added")
  - Be specific about what changed
  - Reference issue/ticket numbers if applicable
  - For larger changes, include a body with more details 

## Common Commit Types:

  - feat: New feature
  - fix: Bug fix
  - docs: Documentation changes
  - style: Code style/formatting
  - refactor: Code change that neither fixes a bug nor adds a feature
  - perf: Performance improvement
  - test: Adding or modifying tests
  - chore: Changes to build process or auxiliary tools

Choose a style that fits your team's workflow and be consistent!


### GitHub Guide for merging current branch to 'main' branch, for pushing progress to 'main' branch-

Q) I wanna push latest progress to 'main' branch, should I push directly or merge with 'jwt-auth'(current branch), where
I pushed the progress already.

- Ans:
  It's generally better to merge your 'jwt-auth' branch(current branch) into main rather than pushing directly to main.
  Here's the recommended workflow:

    - First, make sure all your changes are committed to jwt-auth:
        - git checkout jwt-auth
        - git status # Make sure there are no uncommitted changes

    - Update your local main branch:
        - git checkout main
        - git pull origin main

    - Merge jwt-auth into main:
      -git merge jwt-auth

    - Resolve any merge conflicts if they occur (though there shouldn't be any if main hasn't changed)
    - Push the updated main branch:
        - git push origin main


# Undo the last commit but keep the changes staged:

    git reset --soft HEAD~1




feat(security) is broader and includes:

- Security configurations
- Security filters
- CORS/CSRF settings
- General security best practices
- Encryption/decryption 


feat(auth) is more specific and focuses on:

- Authentication flows
- Login/registration
- JWT/OAuth implementation
- User session management
- Password hashing




