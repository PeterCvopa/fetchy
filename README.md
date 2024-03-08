## About

- simple assignment application which fetches an image, thus the name "Fetchy"

## Notes from the author

- co som bohuzial z casovych dovodov nestihol je spravna modularizacia (vid zadie "(although the app is indeed very small, treat it as a part of big
  production-ready project)") -> preto cela appka mala byt samostany modul

&NewLine;

1. app by okrem Application a main activity nic nemal
2. dependoval by iba na module profile
3. module profile by dependoval na 3 sub moduli
    1. submodul 1. :profile:api -> by mal iba interface provideProfileScreen ktory providoval ProfileScreen interface
    2. submodul 2. :profile:di -> dependency injection module by len obsahoval potrebne veci pre dagger. Podobne ako package di v current App.
    3. submodul 3. :profile:implementation -> kde by bola vsetka logika a test + implementacia provideProfileScreenImpl ktore by vracala composable 
4. :profile: module by potom v gradle api(api sposob priadnia dependencie) :api module a implementoval by :profile:di a :profile:implementation module. 
   Toto by zabespecilo ze z vonku by bola viditelna len method provideProfileScreen a intena implemenaticia by nebola viditelna z App



### Pre technickke interview

- state uptade method for atomic update of state
- channel events ktore prudia z UI
  - co je channel
    - hot 
  - channel vs flow
  - hot vs cold
-repository -> interface  a mak by decaple from the implementation
  - tym padom by sa 