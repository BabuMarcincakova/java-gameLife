# Game Life | Semestrálny projekt
**Autor:** Barbora Marcinčáková  
**Projekt:** Hra Život

---

## Popis:
Hra Život sa hrá na štvorčekovom papieri. Každý štvorček môže byť **biely** alebo **čierny**.  
Na začiatku sa nastaví:
- **rýchlosť generovania novej generácie**, 
- **veľkosť siete**, na ktorej sa hra odohráva, 
- možnosť **zvoliť pravidlá** z pôvodnej hry život, ktoré budú v simulácii zahrnuté.

Následne sa **ručne vyznačia** niektoré štvorčeky na čierno. Potom sa hra odvíja podľa zvolených pravidiel z **Hry Život**.

Simuláciu je možné:
- **zastaviť a opätovne spustiť**, 
- **resetovať** a **znovu nastaviť**, následne opäť spustiť.

---

## Obsah:
Tento priečinok predstavuje **JavaFX projekt**, ktorý je možné spustiť v IntelliJ. Projekt sa spúšťa súborom `GameLife.java`, ktorý obsahuje funkciu `main`.

---

## Ovládanie hry:
Po spustení `main` sa objaví okno s **nastaveniami**, kde si používateľ môže zvoliť:
- **rýchlosť simulácie** z rozsahu **0.1 - 1.5 s**,
- **veľkosť vykresľovaného poľa** z 4 možností veľkosti,
- pomocou **checkboxov** si môže vybrať **pravidlá z Hry Život**, ktoré budú zahrnuté v simulácii.

Po kliknutí na tlačidlo **"Next"** sa používateľ dostane do druhej časti, kde:
- kliknutím do mriežky **vyberie polia**, ktoré budú predstavovať **živé bunky**,
- následne opäť klikne na tlačidlo **"Next"**, čím spustí simuláciu bežiacu podľa doteraz zvolených nastavení.

V tomto štádiu má používateľ k dispozícii dve tlačidlá:
- **"Pause"**: umožňuje zastaviť simuláciu a pri ďalšom kliknutí ju opäť spustiť.
- **"Retry"**: vráti používateľa na základné nastavenia, zruší aktuálnu simuláciu a umožní mu vytvoriť novú.
