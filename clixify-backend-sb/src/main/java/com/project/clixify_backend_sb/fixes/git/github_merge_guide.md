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