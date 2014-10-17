--if bug exists (in contact index) then fix it
update contact set index=10  where index is null;
