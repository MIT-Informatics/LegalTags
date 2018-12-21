%% cmr.pro
%% Formalization of the 201 CMR 17.00: STANDARDS FOR THE PROTECTION OF
%% PERSONAL INFORMATION OF RESIDENTS OF THE COMMONWEALTH

%% %%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Base relations
%%



%% cmr_dataSubjectsInScope(ds)
%% Does the dataset ctonain information about massacusetts residents

%% cmr_personalInformation(DS) Does the dataset contain at least one
%%    record that (a) includes last name; (b) includes first name or
%%    initial; and (c) includes SSN or drivers license or state issued
%%    ID or financial account number

%% cmr_nonPublicInformation(DS) Does the dataset contain at least one
%%    record obtained from a non-public source?

%% cmr_depositorInScope(dd, ds)
%%   Relation holds if the CMR legislation says that depositor dd and  data set ds are in the scope of the legislation


%% cmr_hasUserAuthentication(r).
%%   Relation holds if repository r uses user authentication.

%% cmr_hasUserAccessControl(r).
%%   Relation holds if repository r uses access control to restrict users’ access to data.

%% cmr_hasWrittenSecurityPlan(r).
%%   Relation holds if repository r has an appropriate written security plan

%% cmr_monitorsSystem(r).
%%   Relation holds if repository r monitors the system for unauthorized use or access to data.

%% cmr_patchesSystem(r).
%%   Relation holds if repository r patches the system regularly.

%% cmr_trainsEmployees(r).
%%   Relation holds if repository r trains its employees on proper security protocols.

%% cmr_regularAudits(r).
%%   Relation holds if repository r allows regular security audits.

%% cmr_reportsBreachesToDataOwner(r).
%%   Relation r holds if repository r reports any data breaches to the data owner.

%% cmr_reportsBreachesToDataSubjects(r).
%%   Relation r holds if repository r reports any data breaches to the data subject.

%% cmr_reportsBreachesToGovt(r).
%%   Relation r holds if repository r reports any data breaches to the appropriate government agencies.

%% cmr_destroysRecords(r).
%%   Relation r holds if repository r destroys records appropriately.

%% %%%%%%%%%%%%%%%%%%%%%%%%
%% Constants
%%

%% cmr_StorageEncrypted
%%   This constant denotes the action description that the storage
%%   must be encrypted.

%% cmr_TransmissionEncrypted
%%   This constant denotes the action description that the data must
%%   be encrypted when transmitted to or from a repository.

%% %%%%%%%%%%%%%%%%%%%%%%%%
%% Derived relations
%%
%% cmr_secure(r)
%%   Relation holds if the CMR legislation says that repository r is
%%   sufficiently secure to accept data covered by CMR legislation.
cmr_secure(R) :-
    cmr_hasUserAuthentication(R),
    cmr_hasUserAccessControl(R),
    cmr_hasWrittenSecurityPlan(R),
    cmr_monitorsSystem(R),
    cmr_patchesSystem(R),
    cmr_trainsEmployees(R),
    cmr_regularAudits(R),
    cmr_reportsBreachesToDataOwner(R),
    cmr_reportsBreachesToDataSubjects(R),
    cmr_reportsBreachesToGovt(R),
    cmr_destroysRecords(R).

%% cmr_isAcceptableConditionsForAccept(cs)
%%   Relation holds if condition set cs contains appropriate
%%   conditions to allow the acceptance of a data set by a repository,
%%   for a dataset and depositor covered by the CMR legislation.

cmr_isAcceptableConditionsForAccept(CS) :-
    conditionsRequire(CS, cmr_StorageEncrypted),
    conditionsRequire(CS, cmr_TransmissionEncrypted).

%% cmr_isAcceptableConditionsForRelease(cs)
%%   Relation holds if condition set cs contains appropriate
%%   conditions to allow the release of a data set by a repository,
%%   for a dataset and depositor covered by the CMR legislation.
cmr_isAcceptableConditionsForRelease(CS) :-
    conditionsRequire(CS, cmr_TransmissionEncrypted).


%% cmr_identifiable(ds)
%%   Relation holds if the dataset contains peronal information and nonpublic
%%   information about MA residents
cmr_identifiable(DS) :-
    cmr_dataSubjectsInScope(DS),
    cmr_personalInformation(DS),
    cmr_nonPublicInformation(DS).
    

%% ---------------------------------------------
%% inScope rules. Does CMR claim that an action is in scope?

inScope(cmr, A) :-
    dataSetForAction(A, _R, DS, _DD), 
    cmr_identifable(DS).


%% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% CMR rules for permitted and denied actions
%%
permitted(cmr, deposit(DD, DS, R, CS), N) :-
    bounded(CS, N),
    cmr_depositorInScope(DD, DS),
    cmr_identifiable(DS),
    cmr_secure(R).

permitted(cmr, accept(R, DS, DD, CS), N) :-
    bounded(CS, N),
    cmr_depositorInScope(DD, DS),
    cmr_identifiable(DS),
    cmr_secure(R),
    cmr_isAcceptableConditionsForAccept(CS).

permitted(cmr, release(R, DS, _DU, DD, CS), N) :-
    bounded(CS, N),
    cmr_depositorInScope(DD, DS),
    cmr_identifiable(DS),
    cmr_secure(R),
    cmr_isAcceptableConditionsForRelease(CS).

denied(cmr, deposit(DD, DS, R, CS), N) :-
    bounded(CS, N),
    cmr_depositorInScope(DD, DS),
    cmr_identifiable(DS),
    \+(cmr_secure(R)).

denied(cmr, accept(R, DS, DD, CS), N) :-
    bounded(CS, N),
    cmr_depositorInScope(DD, DS),
    cmr_identifiable(DS),
    \+(cmr_secure(R)).

denied(cmr, accept(R, DS, DD, CS), N) :-
    bounded(CS, N),
    cmr_depositorInScope(DD, DS),
    cmr_identifiable(DS),
    cmr_secure(R),
    \+(cmr_isAcceptableConditionsForAccept(CS)).

denied(cmr, release(R, DS, _DU, DD, CS), N) :-
    bounded(CS, N),
    cmr_depositorInScope(DD, DS),
    cmr_identifiable(DS),
    \+(cmr_secure(R)).

denied(cmr, release(R, DS, _DU, DD, CS), N) :-
    bounded(CS, N),
    cmr_depositorInScope(DD, DS),
    cmr_identifiable(DS),
    cmr_secure(R),
    \+(cmr_isAcceptableConditionsForRelease(CS)).


%% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Relating CMR requirements to DataTags requirements
conditionsRequire(CS, storageEncrypted) :-
    conditionsRequire(CS, cmr_StorageEncrypted).
