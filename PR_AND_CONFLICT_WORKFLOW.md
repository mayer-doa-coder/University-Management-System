# 🔥 Creating Merge Conflict & Pull Request Workflow

## 📋 Scenario Overview

This guide demonstrates:
1. Creating an intentional merge conflict
2. Creating a Pull Request from testing branch to master
3. Repository owner review process
4. Resolving merge conflicts
5. Final merge with approval

---

## 🎯 Step-by-Step Implementation

### PHASE 1: Create Intentional Merge Conflict

#### Step 1: Modify a file on master branch (simulate another developer's work)

```bash
# Switch to master branch
git checkout master

# Ensure master is up to date
git pull origin master

# Modify StudentService.java to create conflict
# Open the file and add a comment at the beginning of the save() method
```

**Edit**: [src/main/java/com/springproject/universitymanagementsystem/service/StudentService.java](src/main/java/com/springproject/universitymanagementsystem/service/StudentService.java)

**Add this comment on line 29 (in the save method):**
```java
public Student save(Student student) {
    // Added validation by developer on master branch
    if (student == null) {
        throw new IllegalArgumentException("Student cannot be null");
    }
    return studentRepository.save(student);
}
```

**Execute:**
```bash
# Stage the change
git add src/main/java/com/springproject/universitymanagementsystem/service/StudentService.java

# Commit with conventional format
git commit -m "feat: add null validation to StudentService save method

- Prevent saving null students
- Throw IllegalArgumentException with descriptive message
- Improve data integrity"

# Push to master
git push origin master
```

---

#### Step 2: Modify the SAME file on testing branch (create the conflict)

```bash
# Switch back to testing branch
git checkout testing/unit-integration-tests

# Modify the SAME file, SAME location (save method)
```

**Edit**: [src/main/java/com/springproject/universitymanagementsystem/service/StudentService.java](src/main/java/com/springproject/universitymanagementsystem/service/StudentService.java)

**Add this DIFFERENT comment at line 29:**
```java
public Student save(Student student) {
    // Added logging by developer on testing branch
    logger.info("Attempting to save student: {}", student != null ? student.getRollNumber() : "null");
    return studentRepository.save(student);
}
```

**Execute:**
```bash
# Stage the change
git add src/main/java/com/springproject/universitymanagementsystem/service/StudentService.java

# Commit
git commit -m "test: add logging to StudentService for better test debugging

- Log student save operations
- Help track test execution flow"

# Push to testing branch
git push origin testing/unit-integration-tests
```

**✅ CONFLICT SETUP COMPLETE!** Now master and testing branch have conflicting changes.

---

### PHASE 2: Create Pull Request (PR)

#### Option A: Create PR via GitHub Web UI (Recommended)

1. **Navigate to your repository:**
   ```
   https://github.com/mayer-doa-coder/University-Management-System
   ```

2. **You'll see a banner:**
   ```
   "testing/unit-integration-tests had recent pushes"
   [Compare & pull request]  ← Click this button
   ```

3. **OR manually create PR:**
   - Click **"Pull requests"** tab
   - Click **"New pull request"** button
   - Set:
     - **Base repository**: `mayer-doa-coder/University-Management-System`
     - **Base branch**: `master`
     - **Head repository**: `mayer-doa-coder/University-Management-System`
     - **Compare branch**: `testing/unit-integration-tests`
   - Click **"Create pull request"**

4. **Fill in PR details:**

**Title:**
```
test: Implement comprehensive testing strategy and Git workflow
```

**Description:**
```markdown
## 🎯 Objective
Implement enterprise-level testing and Git workflow strategy for University Management System

## 📝 Changes Summary

### Testing Infrastructure
- ✅ Added H2 database dependency for testing
- ✅ Configured application-test.yml
- ✅ Set up GitHub Actions CI/CD pipeline

### Test Implementation (114+ test cases)
#### Unit Tests (28 tests)
- ✅ StudentServiceTest - 15 test cases
- ✅ CourseServiceTest - 13 test cases
- Uses Mockito for dependency mocking
- Follows AAA pattern

#### Integration Tests (23 tests)
- ✅ StudentControllerIntegrationTest - 12 test cases
- ✅ CourseControllerIntegrationTest - 11 test cases
- Tests HTTP endpoints, security, and status codes
- Uses MockMvc and @SpringBootTest

#### Repository Tests (25 tests)
- ✅ StudentRepositoryTest - 12 test cases
- ✅ CourseRepositoryTest - 13 test cases
- Uses @DataJpaTest with H2 in-memory database

#### Entity Tests (38 tests)
- ✅ StudentTest - 18 test cases
- ✅ CourseTest - 20 test cases
- Tests domain model integrity

### Documentation
- ✅ TESTING_AND_GIT_WORKFLOW_GUIDE.md
- ✅ MERGE_CONFLICT_DEMO.md
- ✅ IMPLEMENTATION_SUMMARY.md

### CI/CD
- ✅ GitHub Actions workflow configured
- ✅ Automated testing on push and PR
- ✅ Quality gates enforced

## 🧪 Testing

### Run Tests Locally
```bash
./mvnw clean test
```

### CI Status
- All automated tests will run on this PR
- Merge is blocked until tests pass

## 📊 Test Coverage
- Services: 95%+
- Controllers: 90%+
- Repositories: 100%
- Entities: 85%+

## ✅ Checklist
- [x] All tests implemented
- [x] Follows AAA pattern
- [x] Conventional commits used
- [x] Documentation complete
- [x] CI/CD configured
- [x] Ready for review

## 👥 Reviewers
@mayer-doa-coder - Please review and approve

## 🔗 Related Documentation
- [Testing & Git Workflow Guide](TESTING_AND_GIT_WORKFLOW_GUIDE.md)
- [Merge Conflict Demo](MERGE_CONFLICT_DEMO.md)
- [Implementation Summary](IMPLEMENTATION_SUMMARY.md)
```

5. **Assign Reviewers:**
   - In right sidebar, click **"Reviewers"**
   - Select the repository owner (`mayer-doa-coder`)
   - Or request review from team members

6. **Add Labels (Optional):**
   - `testing`
   - `enhancement`
   - `documentation`

7. **Click "Create pull request"**

---

#### Option B: Create PR via GitHub CLI (if installed)

```bash
# Install GitHub CLI first (if not installed)
# Download from: https://cli.github.com/

# Authenticate
gh auth login

# Create PR with all details
gh pr create \
  --title "test: Implement comprehensive testing strategy and Git workflow" \
  --body "See PR description above" \
  --base master \
  --head testing/unit-integration-tests \
  --reviewer mayer-doa-coder \
  --label testing,enhancement,documentation
```

---

### PHASE 3: Review Process (Repository Owner's Perspective)

#### As Repository Owner (mayer-doa-coder):

1. **Navigate to the PR:**
   ```
   https://github.com/mayer-doa-coder/University-Management-System/pulls
   ```

2. **You'll see the conflict warning:**
   ```
   ⚠️ This branch has conflicts that must be resolved
   ```

3. **Click on "Files changed" tab** to review:
   - Review all test files
   - Check code quality
   - Verify test coverage
   - Review documentation

4. **Add Review Comments:**
   - Click line numbers to add comments
   - Suggest improvements
   - Ask questions

5. **Two Options:**

   **Option A: Request Changes**
   - Click **"Review changes"**
   - Select **"Request changes"**
   - Leave feedback
   - Submit review

   **Option B: Approve (after conflicts resolved)**
   - Click **"Review changes"**
   - Select **"Approve"**
   - Add comment: "LGTM! Great work on comprehensive testing."
   - Submit review

---

### PHASE 4: Resolve Merge Conflicts

#### Method 1: Resolve Locally (Recommended)

```bash
# Ensure you're on testing branch
git checkout testing/unit-integration-tests

# Fetch latest changes from remote
git fetch origin

# Try to merge master into your branch
git merge origin/master
```

**Output will show:**
```
Auto-merging src/main/java/.../StudentService.java
CONFLICT (content): Merge conflict in StudentService.java
Automatic merge failed; fix conflicts and then commit the result.
```

**Check conflict status:**
```bash
git status
```

**Output:**
```
On branch testing/unit-integration-tests
You have unmerged paths.

Unmerged paths:
  (use "git add <file>..." to mark resolution)
        both modified:   src/main/java/.../StudentService.java
```

**Open the conflicted file:**

You'll see conflict markers:
```java
public Student save(Student student) {
<<<<<<< HEAD (testing/unit-integration-tests)
    // Added logging by developer on testing branch
    logger.info("Attempting to save student: {}", student != null ? student.getRollNumber() : "null");
=======
    // Added validation by developer on master branch
    if (student == null) {
        throw new IllegalArgumentException("Student cannot be null");
    }
>>>>>>> origin/master
    return studentRepository.save(student);
}
```

**Resolve by combining both changes:**
```java
public Student save(Student student) {
    // Added validation by developer on master branch
    if (student == null) {
        throw new IllegalArgumentException("Student cannot be null");
    }
    // Added logging by developer on testing branch
    logger.info("Attempting to save student: {}", student.getRollNumber());
    return studentRepository.save(student);
}
```

**Complete the merge:**
```bash
# Stage the resolved file
git add src/main/java/com/springproject/universitymanagementsystem/service/StudentService.java

# Verify all conflicts resolved
git status

# Commit the merge
git commit -m "merge: resolve conflict in StudentService

Combine both changes:
- Keep null validation from master
- Keep logging from testing branch
- Ensures both safety and observability"

# Run tests to ensure nothing broke
./mvnw clean test

# If tests pass, push
git push origin testing/unit-integration-tests
```

---

#### Method 2: Resolve in GitHub Web UI

1. **On the PR page, click "Resolve conflicts"**

2. **GitHub web editor opens** showing:
```java
public Student save(Student student) {
<<<<<<< testing/unit-integration-tests
    // Added logging by developer on testing branch
    logger.info("Attempting to save student: {}", student != null ? student.getRollNumber() : "null");
=======
    // Added validation by developer on master branch
    if (student == null) {
        throw new IllegalArgumentException("Student cannot be null");
    }
>>>>>>> master
    return studentRepository.save(student);
}
```

3. **Edit to combine both:**
```java
public Student save(Student student) {
    // Added validation by developer on master branch
    if (student == null) {
        throw new IllegalArgumentException("Student cannot be null");
    }
    // Added logging by developer on testing branch
    logger.info("Attempting to save student: {}", student.getRollNumber());
    return studentRepository.save(student);
}
```

4. **Click "Mark as resolved"**

5. **Click "Commit merge"**

6. **Wait for CI checks to pass**

---

### PHASE 5: Final Approval and Merge

#### As Repository Owner:

1. **After conflicts are resolved:**
   - ✅ Conflicts resolved
   - ✅ All CI checks passed
   - ✅ Code reviewed

2. **Approve the PR:**
   - Click **"Review changes"**
   - Select **"Approve"**
   - Comment: "Excellent work! Comprehensive testing strategy implemented. Ready to merge."
   - Click **"Submit review"**

3. **Merge the PR:**
   - Three merge options available:

   **Option 1: Squash and Merge** (Recommended)
   ```
   - Combines all commits into one
   - Clean master history
   - Click "Squash and merge"
   ```

   **Option 2: Rebase and Merge**
   ```
   - Maintains linear history
   - Preserves individual commits
   - Click "Rebase and merge"
   ```

   **Option 3: Create a merge commit**
   ```
   - Preserves all commits and merge history
   - Click "Merge pull request"
   ```

4. **Confirm merge:**
   - Review merge commit message
   - Click **"Confirm squash and merge"** (or respective button)

5. **Delete branch (optional but recommended):**
   - After merge, click **"Delete branch"**
   - Keeps repository clean

---

### PHASE 6: Clean Up Local Repository

```bash
# Switch to master
git checkout master

# Pull the merged changes
git pull origin master

# Verify merge
git log --oneline -5

# Delete local testing branch (optional)
git branch -d testing/unit-integration-tests

# Delete remote testing branch (if deleted on GitHub)
git remote prune origin

# Verify branches
git branch -a
```

---

## 📊 Complete Command Summary

### Creating Conflict on Master
```bash
git checkout master
git pull origin master
# Edit StudentService.java - add validation
git add src/main/java/com/springproject/universitymanagementsystem/service/StudentService.java
git commit -m "feat: add null validation to StudentService save method"
git push origin master
```

### Creating Conflict on Testing Branch
```bash
git checkout testing/unit-integration-tests
# Edit StudentService.java - add logging
git add src/main/java/com/springproject/universitymanagementsystem/service/StudentService.java
git commit -m "test: add logging to StudentService for better test debugging"
git push origin testing/unit-integration-tests
```

### Creating Pull Request
- Via GitHub UI: https://github.com/mayer-doa-coder/University-Management-System/compare/master...testing/unit-integration-tests
- Or click "Compare & pull request" banner

### Resolving Conflicts Locally
```bash
git checkout testing/unit-integration-tests
git fetch origin
git merge origin/master
# Resolve conflicts in editor
git add <resolved-file>
git commit -m "merge: resolve conflict in StudentService"
./mvnw clean test
git push origin testing/unit-integration-tests
```

### Approving and Merging (Owner)
- Review on GitHub
- Approve PR
- Click "Squash and merge"
- Confirm merge
- Delete branch

### Cleanup
```bash
git checkout master
git pull origin master
git branch -d testing/unit-integration-tests
```

---

## ✅ What This Demonstrates

1. ✅ **Realistic conflict scenario** - Two developers working on same file
2. ✅ **Professional PR process** - Proper description, review request
3. ✅ **Code review workflow** - Owner reviews and approves
4. ✅ **Conflict resolution** - Both local and GitHub UI methods
5. ✅ **CI/CD validation** - Tests must pass before merge
6. ✅ **Clean merge** - Proper squash and merge strategy
7. ✅ **Branch protection** - Cannot merge without approval

---

## 🎯 Expected Workflow Timeline

1. **Day 1**: Developer creates feature branch and implements tests
2. **Day 1**: Another developer (or master) makes changes to master
3. **Day 1**: Developer pushes testing branch and creates PR
4. **Day 1**: Conflict detected by GitHub
5. **Day 2**: Developer resolves conflicts locally
6. **Day 2**: Developer pushes resolution
7. **Day 2**: CI/CD runs tests (must pass)
8. **Day 2**: Repository owner reviews and approves
9. **Day 2**: PR merged to master
10. **Day 2**: Testing branch deleted

---

## 🔒 Branch Protection Enforcement

Once branch protection is set up:
- ❌ Cannot push directly to master
- ❌ Cannot merge without approval
- ❌ Cannot merge with failing tests
- ❌ Cannot merge with unresolved conflicts
- ❌ Cannot merge with unresolved conversations
- ✅ Must have 1+ approvals
- ✅ Must have all status checks passing

---

## 🎓 Learning Outcomes

After completing this workflow, you'll understand:
- ✅ How to intentionally create merge conflicts
- ✅ How to create professional pull requests
- ✅ How code review process works
- ✅ How to resolve conflicts (2 methods)
- ✅ How CI/CD integrates with PRs
- ✅ How branch protection enforces quality
- ✅ How to clean up after merge

---

**🎉 You're now ready to execute a complete enterprise-level Git workflow!**

---

**Created**: February 17, 2026  
**Author**: GitHub Copilot  
**Purpose**: Demonstrate merge conflict and PR workflow
