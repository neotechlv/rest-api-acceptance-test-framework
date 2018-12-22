# Release process

The release process is automated as a Travis deployment. 

## Steps

1. Ensure that the master branch is building and that tests are passing.
1. Create a new release on GitHub. **The tag name is used as the version**, so please enter version like `1.2.3`).
1. Check that the Travis build passed.
1. Publishing of the released artifacts to Bintray is fully automated. Once the Travis build completes, there are no further actions to perform on the repository.

## Internal details

* Travis secrets hold Bintray username/key that are used for publishing.
