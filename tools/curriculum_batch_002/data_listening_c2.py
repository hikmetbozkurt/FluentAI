#!/usr/bin/env python3
"""
Listening Batch 002: C2 Scenarios (9 scenarios).
"""

from listening_builder import build_scenario

SCENARIOS_C2 = [
    # 1. diplomatic-summit-bilateral-accord (international-relations / travel)
    build_scenario(
        "listening.c2.diplomatic-summit-bilateral-accord",
        "Negotiating Strategic Bilateral Maritime Boundaries at High-Level Diplomatic Plenary",
        "C2", "negotiation",
        "Ambassador Montgomery and Chief Diplomatic Envoy Leyla negotiate maritime exclusive economic zone delimitations and joint hydrocarbon exploration protocols during closed-door treaty talks.",
        [
            {"id": "montgomery", "name": "Ambassador Montgomery", "role": "Special Envoy for Maritime Affairs", "accent": "British"},
            {"id": "leyla", "name": "Leyla", "role": "Chief Diplomatic Plenipotentiary", "accent": "Turkish"},
        ],
        "c2_diplomatic_summit_bilateral_accord.mp3",
        [
            {"speaker_id": "montgomery", "text_en": "Ambassador Demir, our respective heads of state have entrusted us with unknotting thirty years of jurisdictional friction regarding continental shelf delimitations.", "text_tr": "Büyükelçi Demir, devlet başkanlarımız kıta sahanlığı sınırlandırmalarına ilişkin otuz yıllık yetki alanı sürtüşmesini çözme görevini bize tevdi etti."},
            {"speaker_id": "leyla", "text_en": "Indeed, Arthur. But let us be unequivocal: my government cannot acquiesce to median-line equidistance formulas that treat uninhabited insular outcrops as having full maritime projection.", "text_tr": "Gerçekten öyle Arthur. Ancak açık konuşalım: Hükümetim, ıssız ada çıkıntılarına tam deniz projeksiyonu tanıyan eşit uzaklık formüllerine razı gelemez."},
            {"speaker_id": "montgomery", "text_en": "Under the Law of the Sea jurisprudence, islands naturally possess territorial seas, contiguous zones, and continental shelves unless equitable principles dictate special circumstance enclaving.", "text_tr": "Deniz Hukuku içtihadı uyarınca, hakkaniyet ilkeleri özel durum anklavlamasını zorunlu kılmadıkça adalar doğal olarak karasularına, bitişik bölgelere ve kıta sahanlıklarına sahiptir."},
            {"speaker_id": "leyla", "text_en": "And International Court of Justice precedents repeatedly affirm that cut-off effects against extensive mainland coastlines constitute prima facie grounds for modifying median lines toward proportionality.", "text_tr": "Ve Uluslararası Adalet Divanı emsal kararları, geniş anakara kıyı şeritlerine karşı kesme etkilerinin, orta hatların orantılılığa doğru değiştirilmesi için ilk bakışta haklı gerekçe teşkil ettiğini defaatle teyit etmektedir."},
            {"speaker_id": "montgomery", "text_en": "If we contest sovereignty ad infinitum, capital investment in offshore clean energy and subsea interconnects remains entirely paralyzed. What creative condominium structure do you propose?", "text_tr": "Egemenliği sonsuza dek tartışırsak, açık deniz temiz enerjisi ve denizaltı ara bağlantılarına yönelik sermaye yatırımı tamamen felç kalır. Nasıl bir yaratıcı ortak yönetim yapısı öneriyorsunuz?"},
            {"speaker_id": "leyla", "text_en": "A fifty-year joint development zone without prejudice to final sovereign demarcations. Revenues from offshore wind concessions and seabed mineral exploration would be escrowed in equal halves.", "text_tr": "Nihai egemenlik sınırlandırmalarına halel getirmeksizin elli yıllık bir ortak kalkınma bölgesi. Açık deniz rüzgar imtiyazlarından ve deniz yatağı maden aramasından elde edilecek gelirler eşit yarılar halinde emanet hesabında tutulacaktır."},
            {"speaker_id": "montgomery", "text_en": "With bilateral environmental oversight commissions possessing mutual veto authority over marine biosphere preservation?", "text_tr": "Deniz biyosferinin korunması konusunda karşılıklı veto yetkisine sahip ikili çevre denetim komisyonlarıyla birlikte mi?"},
            {"speaker_id": "leyla", "text_en": "Precisely. That decoupled mechanism decouples commercial exploitation from geopolitical prestige, allowing both cabinets to declare a diplomatic triumph domestically.", "text_tr": "Kesinlikle. Bu ayrıştırılmış mekanizma, ticari işletmeyi jeopolitik prestijden ayırarak her iki kabinenin de içeride diplomatik bir zafer ilan etmesine olanak tanır."},
        ],
        [
            {
                "question_en": "What legal principle does Leyla cite to counter the strict median-line equidistance formula?",
                "question_tr_hint": "Leyla katı eşit uzaklık formülüne karşı çıkmak için hangi hukuki ilkeye atıfta bulunuyor?",
                "correct_answer": "ICJ precedents establishing that coastline cut-off effects justify modifying median lines for proportionality.",
                "distractors": [
                    "A unilateral naval declaration of war against neighboring maritime territories.",
                    "An ancient imperial treaty signed before modern navigation instruments existed.",
                    "The complete abolition of all maritime sovereign zones under international charters."
                ],
                "explanation_en": "Leyla cites ICJ precedents confirming cut-off effects against mainland coastlines justify proportionality adjustments.",
                "explanation_tr": "Leyla anakara kıyılarındaki kesme etkilerinin orta hatların orantılılığa göre revize edilmesini haklı kıldığını belirtir."
            },
            {
                "question_en": "What economic paralysis does Ambassador Montgomery highlight if territorial disputes remain unresolved?",
                "question_tr_hint": "Büyükelçi Montgomery toprak ihtilaflarının çözümsüz kalması durumunda hangi ekonomik felci vurguluyor?",
                "correct_answer": "Complete freezing of capital investment in offshore renewable energy and subsea power interconnects.",
                "distractors": [
                    "The immediate collapse of all domestic currency exchange markets.",
                    "The total shutdown of all municipal airline operations internationally.",
                    "Mandatory rationing of drinking water across both nations."
                ],
                "explanation_en": "Montgomery notes capital investment in offshore energy and subsea interconnects remains paralyzed.",
                "explanation_tr": "Montgomery açık deniz enerjisi ve denizaltı bağlantılarına yönelik sermaye yatırımlarının donacağını vurgular."
            },
            {
                "question_en": "What innovative structural compromise does Leyla table during plenary discussions?",
                "question_tr_hint": "Leyla genel kurul görüşmeleri sırasında hangi yenilikçi yapısal uzlaşmayı masaya yatırıyor?",
                "correct_answer": "A 50-year joint development zone sharing resource revenues equally without prejudicing sovereign claims.",
                "distractors": [
                    "Ceding all disputed oceanic waters entirely to private multinational corporations.",
                    "Submerging all disputed islands beneath artificial concrete seawalls.",
                    "Inviting foreign military superpowers to establish permanent occupation garrisons."
                ],
                "explanation_en": "Leyla proposes a 50-year joint development zone with revenues escrowed in equal halves without prejudice to sovereignty.",
                "explanation_tr": "Leyla egemenlik iddialarını saklı tutarak gelirlerin yarı yarıya paylaşıldığı 50 yıllık ortak kalkınma bölgesi önerir."
            },
            {
                "question_en": "What institutional safeguard does Montgomery append to the joint maritime framework?",
                "question_tr_hint": "Montgomery ortak denizcilik çerçevesine hangi kurumsal güvenlik önlemini ekliyor?",
                "correct_answer": "A bilateral environmental commission with mutual veto powers over ecological and biosphere conservation.",
                "distractors": [
                    "A mandatory naval artillery duel conducted annually between defense ministers.",
                    "An automated submarine drone fleet programmed to destroy commercial fishing vessels.",
                    "A joint commercial casino operated in international waters."
                ],
                "explanation_en": "Montgomery specifies bilateral environmental commissions possessing mutual veto authority.",
                "explanation_tr": "Montgomery çevre denetim komisyonlarının karşılıklı veto yetkisine sahip olmasını şart koşar."
            },
            {
                "question_en": "How does Leyla characterize the political advantage of this proposed diplomatic treaty?",
                "question_tr_hint": "Leyla önerilen bu diplomatik anlaşmanın siyasi avantajını nasıl nitelendiriyor?",
                "correct_answer": "It decouples resource economics from geopolitical prestige, allowing both governments to claim victory.",
                "distractors": [
                    "It ensures that only one nation receives financial benefit while humiliating the other.",
                    "It bankrupts both competing national ministries to enforce global pacifism.",
                    "It conceals all treaty terms from public and legislative scrutiny forever."
                ],
                "explanation_en": "Leyla explains that decoupling commercial exploitation from prestige lets both cabinets declare triumph.",
                "explanation_tr": "Leyla ticari işletmeyi prestijden ayırmanın her iki hükümete de içeride zafer ilan etme fırsatı verdiğini söyler."
            }
        ],
        ["international_relations", "travel"]
    ),

    # 2. central-bank-monetary-policy-briefing (business / economics)
    build_scenario(
        "listening.c2.central-bank-monetary-policy-briefing",
        "Executive Briefing on Sovereign Quantitative Tightening and Liquidity Dynamics",
        "C2", "executive_briefing",
        "Monetary Policy Committee Governor Thorne and Senior Macroeconomic Strategist Emre evaluate sovereign debt yield-curve steepening, collateral velocity, and reverse repo run-off rates.",
        [
            {"id": "thorne", "name": "Governor Thorne", "role": "Central Bank Governor", "accent": "American"},
            {"id": "emre", "name": "Emre", "role": "Senior Macroeconomic Policy Director", "accent": "Turkish"},
        ],
        "c2_central_bank_monetary_policy_briefing.mp3",
        [
            {"speaker_id": "thorne", "text_en": "Emre, ahead of Wednesday's press conference, the rate-setting committee requires a definitive assessment of whether accelerating our balance-sheet runoff risks an acute liquidity seizure in short-term repo markets.", "text_tr": "Emre, çarşamba günkü basın toplantısı öncesinde faiz belirleme komitesi, bilanço küçültmemizi hızlandırmanın kısa vadeli repo piyasalarında akut bir likidite krizini tetikleme riski taşıyıp taşımadığına dair kesin bir değerlendirme istiyor."},
            {"speaker_id": "emre", "text_en": "Governor Thorne, our money-market desk indicators suggest we are rapidly approaching the lowest comfortable level of reserves. Overnight reverse repo balances have shrunk from two trillion dollars to under two hundred billion.", "text_tr": "Başkan Thorne, para piyasası masası göstergelerimiz en rahat rezerv seviyesine hızla yaklaştığımıza işaret ediyor. Gecelik ters repo bakiyeleri iki trilyon dolardan iki yüz milyar doların altına geriledi."},
            {"speaker_id": "thorne", "text_en": "And once that reverse repo buffer is completely exhausted, further asset runoff drains reserves directly from commercial bank balance sheets, impairing interbank collateral velocity.", "text_tr": "Ve bu ters repo tamponu tamamen tükendiğinde, daha fazla varlık çıkışı rezervleri doğrudan ticari banka bilançolarından çekerek bankalar arası teminat hızını bozar."},
            {"speaker_id": "emre", "text_en": "Precisely. We are already observing episodic spikes in the Secured Overnight Financing Rate above the interest on reserve balances during month-end settlement dates.", "text_tr": "Kesinlikle. Ay sonu takas tarihlerinde Güvenceli Gecelik Finansman Oranında rezerv bakiyelerine uygulanan faizin üzerinde dönemsel sıçramalar gözlemliyoruz."},
            {"speaker_id": "thorne", "text_en": "Which mirrors the precursor stresses of the September twenty-nineteen funding freeze. What recalibration of our terminal asset caps do you advise?", "text_tr": "Bu da Eylül iki bin on dokuzdaki fonlama krizinin öncül streslerini yansıtıyor. Nihai varlık üst sınırlarımızda nasıl bir yeniden kalibrasyon önerirsiniz?"},
            {"speaker_id": "emre", "text_en": "We should taper the monthly Treasury runoff cap by half, from sixty billion to thirty billion, while keeping our Standing Repo Facility fully operational as a permanent liquidity backstop.", "text_tr": "Daimi Repo Kolaylığımızı kalıcı bir likidite desteği olarak tamamen faal tutarken, aylık Hazine varlık çıkış sınırını yarıya indirerek altmış milyardan otuz milyara düşürmeliyiz."},
            {"speaker_id": "thorne", "text_en": "Will halting mortgage-backed security runoff trigger allegations from hawkish legislators that the central bank is prematurely capitulating on inflation containment?", "text_tr": "İpoteğe dayalı menkul kıymet çıkışını durdurmak, şahin parlamenterlerin merkez bankasının enflasyonu dizginleme konusunda erkenden teslim olduğu yönündeki suçlamalarını tetikler mi?"},
            {"speaker_id": "emre", "text_en": "We must frame the adjustment strictly as operational plumbing optimization designed to safeguard financial stability, completely independent of our benchmark interest-rate trajectory.", "text_tr": "Bu ayarlamayı, gösterge faiz oranı rotamızdan tamamen bağımsız, finansal istikrarı korumak için tasarlanmış operasyonel bir mekanizma optimizasyonu olarak çerçevelemeliyiz."},
        ],
        [
            {
                "question_en": "What money-market liquidity warning sign does Emre report to the Governor?",
                "question_tr_hint": "Emre Başkana hangi para piyasası likidite uyarı işaretini bildiriyor?",
                "correct_answer": "Overnight reverse repo balances have contracted precipitously from two trillion to under two hundred billion.",
                "distractors": [
                    "Commercial banks have ceased accepting physical currency banknotes.",
                    "The national mint has completely run out of copper paper supplies.",
                    "International credit rating agencies have downgraded all sovereign nations."
                ],
                "explanation_en": "Emre reports overnight reverse repo balances shrank from $2T to under $200B.",
                "explanation_tr": "Emre gecelik ters repo bakiyelerinin 2 trilyon dolardan 200 milyar doların altına gerilediğini açıklar."
            },
            {
                "question_en": "What consequence occurs once the reverse repo facility buffer is depleted?",
                "question_tr_hint": "Ters repo kolaylığı tamponu tükendiğinde nasıl bir sonuç ortaya çıkar?",
                "correct_answer": "Quantitative runoff drains liquidity directly from commercial bank reserves, degrading collateral velocity.",
                "distractors": [
                    "All commercial mortgages are automatically forgiven by executive decree.",
                    "The stock market ceases trading permanently across all global exchanges.",
                    "Interest rates drop to negative one hundred percent immediately."
                ],
                "explanation_en": "Thorne explains further runoff drains reserves directly from bank balance sheets, impairing velocity.",
                "explanation_tr": "Thorne daha fazla küçülmenin rezervleri banka bilançolarından çekip teminat hızını bozacağını belirtir."
            },
            {
                "question_en": "What historic monetary episode does Governor Thorne cite as a cautionary benchmark?",
                "question_tr_hint": "Başkan Thorne uyarıcı bir kıstas olarak hangi tarihi parasal olaya atıfta bulunuyor?",
                "correct_answer": "The September 2019 repo funding crisis and interbank liquidity freeze.",
                "distractors": [
                    "The Dutch Tulip Bulb Mania of the seventeenth century.",
                    "The Great Depression bank closures of nineteen thirty-three.",
                    "The post-war hyperinflation of the Weimar Republic in 1923."
                ],
                "explanation_en": "Thorne remarks that the current stress mirrors the precursor stresses of September 2019.",
                "explanation_tr": "Thorne durumun Eylül 2019 fonlama krizinin öncül streslerini yansıttığını söyler."
            },
            {
                "question_en": "What specific adjustment to Treasury runoff does Emre advocate?",
                "question_tr_hint": "Emre Hazine varlık çıkışında hangi özel ayarlamayı savunuyor?",
                "correct_answer": "Tapering the monthly Treasury redemption cap from sixty billion down to thirty billion dollars.",
                "distractors": [
                    "Quadrupling the runoff cap to two hundred billion dollars monthly.",
                    "Permanently burning all sovereign Treasury bond certificates.",
                    "Banning all commercial banks from holding sovereign debt assets."
                ],
                "explanation_en": "Emre suggests tapering the Treasury runoff cap from $60B to $30B.",
                "explanation_tr": "Emre aylık Hazine varlık çıkış sınırının 60 milyardan 30 milyar dolara indirilmesini önerir."
            },
            {
                "question_en": "How does Emre recommend communicating this policy recalibration to avoid political accusations?",
                "question_tr_hint": "Emre siyasi suçlamaları önlemek için bu politika ayarlamasının nasıl iletilmesini öneriyor?",
                "correct_answer": "As an operational financial stability adjustment distinct from the benchmark policy rate path.",
                "distractors": [
                    "By issuing a false statement denying that any policy meeting took place.",
                    "By publicly attacking members of parliament during the press briefing.",
                    "By resigning from the central bank immediately without explanation."
                ],
                "explanation_en": "Emre stresses framing it as plumbing optimization to safeguard stability, independent of rates.",
                "explanation_tr": "Emre bunun faiz patikasından bağımsız, istikrar odaklı teknik bir iyileştirme olarak sunulmasını önerir."
            }
        ],
        ["business", "economics"]
    ),

    # 3. multinational-board-hostile-takeover (work-career / governance)
    build_scenario(
        "listening.c2.multinational-board-hostile-takeover",
        "Defending Against Leveraged Proxy Battles and Hostile Takeover Incursions",
        "C2", "executive_briefing",
        "Corporate Governance Chair Eleanor and Chief Executive Officer Cenk devise poison-pill defenses, staggered board maneuvers, and white-knight consortium options to thwart activist shareholder raids.",
        [
            {"id": "eleanor", "name": "Eleanor", "role": "Senior Independent Board Chair", "accent": "British"},
            {"id": "cenk", "name": "Cenk", "role": "Group Chief Executive Officer", "accent": "Turkish"},
        ],
        "c2_multinational_board_hostile_takeover.mp3",
        [
            {"speaker_id": "eleanor", "text_en": "Cenk, activist hedge fund Apex Meridian has crossed the nine percent beneficial ownership threshold and launched a hostile tender offer at a thirty percent discount to our intrinsic enterprise valuation.", "text_tr": "Cenk, aktivist koruma fonu Apex Meridian yüzde dokuzluk gerçek yararlanıcı eşiğini aştı ve içsel kurumsal değerlememize kıyasla yüzde otuz indirimli bir düşmanca devralma teklifi başlattı."},
            {"speaker_id": "cenk", "text_en": "Their playbook is transparent, Eleanor: acquire control, break up our core R&D division, strip intellectual property assets, and leverage the balance sheet to pay an extraordinary dividend.", "text_tr": "Oyun planları çok şeffaf Eleanor: kontrolü ele geçir, ana Ar-Ge birimimizi parçala, fikri mülkiyet varlıklarını soy ve olağanüstü temettü ödemek için bilançoyu borca batır."},
            {"speaker_id": "eleanor", "text_en": "We must act decisively before proxy advisory firms issue recommendations to institutional pension funds. What defensive legal mechanisms are viable under our corporate charter?", "text_tr": "Vekalet danışmanlık firmaları kurumsal emeklilik fonlarına tavsiyelerde bulunmadan önce kararlı bir şekilde hareket etmeliyiz. Şirket esas sözleşmemiz kapsamında hangi savunmacı yasal mekanizmalar uygulanabilir?"},
            {"speaker_id": "cenk", "text_en": "First, our board must immediately activate our shareholder rights plan—the flip-in poison pill. If Apex crosses twelve percent, all other shareholders are entitled to purchase common shares at a fifty percent discount.", "text_tr": "İlk olarak, yönetim kurulumuz derhal hissedar hakları planımızı, yani içe katlamalı zehir hapını devreye sokmalıdır. Apex yüzde on ikiyi aşarsa, diğer tüm hissedarlar yüzde elli indirimle adi hisse satın alma hakkına sahip olur."},
            {"speaker_id": "eleanor", "text_en": "Diluting the raider's stake ruinously. But Delaware Chancery Court jurisprudence requires us to demonstrate proportionality under the Unocal doctrine.", "text_tr": "Akıncının payını yıkıcı bir şekilde seyrelterek. Ancak Delaware Yargı Mahkemesi içtihadı Unocal doktrini uyarınca orantılılık göstermemizi şart koşuyor."},
            {"speaker_id": "cenk", "text_en": "We satisfy Unocal because their coercive tender offer severely undervalues our quantum cryptographic pipeline. Simultaneously, we should solicit a competing bid from a friendly sovereign wealth fund.", "text_tr": "Unocal şartını karşılıyoruz çünkü onların baskıcı devralma teklifi kuantum kriptografik işlem hattımızın değerini ciddi şekilde düşürüyor. Eşzamanlı olarak, dost bir ulusal varlık fonundan rakip bir teklif istemeliyiz."},
            {"speaker_id": "eleanor", "text_en": "A white-knight investor consortium that guarantees our research autonomy and provides patient equity capital without predatory asset stripping.", "text_tr": "Araştırma özerkliğimizi garanti eden ve yağmacı varlık soygunu olmaksızın sabırlı özsermaye sağlayan bir beyaz şövalye yatırımcı konsorsiyumu."},
            {"speaker_id": "cenk", "text_en": "Exactly. Let us summon special legal counsel and table the poison pill resolution for an immediate board vote this afternoon.", "text_tr": "Kesinlikle. Özel hukuk danışmanını çağıralım ve bu öğleden sonra acil bir yönetim kurulu oylaması için zehir hapı kararını masaya yatıralım."},
        ],
        [
            {
                "question_en": "What predatory intent does Cenk attribute to the activist hedge fund's hostile raid?",
                "question_tr_hint": "Cenk aktivist fonun düşmanca baskınına hangi yağmacı niyeti atfediyor?",
                "correct_answer": "Dismantling R&D, stripping intellectual property, and overleveraging the corporate balance sheet for dividends.",
                "distractors": [
                    "Investing billions to double employee salaries across all global factories.",
                    "Converting the corporation into a non-profit international charity organization.",
                    "Surrendering all commercial software licenses to open-source communities."
                ],
                "explanation_en": "Cenk explains their plan is to break up R&D, strip IP, and leverage the balance sheet for dividends.",
                "explanation_tr": "Cenk planlarının Ar-Ge'yi dağıtmak, fikri mülkiyeti soymak ve temettü için borçlanmak olduğunu söyler."
            },
            {
                "question_en": "How does the proposed flip-in poison pill function to deter the hostile suitor?",
                "question_tr_hint": "Önerilen içe katlamalı zehir hapı düşmanca talipliyi caydırmak için nasıl işler?",
                "correct_answer": "It permits all shareholders except the raider to buy deeply discounted stock if a threshold is crossed.",
                "distractors": [
                    "It physically dissolves all shareholder share certificates using acid.",
                    "It transfers the company's legal headquarters to an uncharted Arctic island.",
                    "It mandates that board members challenge the raider to physical boxing matches."
                ],
                "explanation_en": "Cenk states if Apex crosses 12%, all other shareholders can buy shares at a 50% discount, diluting them.",
                "explanation_tr": "Cenk Apex %12'yi geçerse diğer hissedarların %50 indirimle hisse alarak saldırganı seyrelteceğini belirtir."
            },
            {
                "question_en": "What legal standard must the board satisfy under Delaware Chancery Court jurisprudence?",
                "question_tr_hint": "Yönetim kurulu Delaware Yargı Mahkemesi içtihadı kapsamında hangi yasal standardı karşılamalıdır?",
                "correct_answer": "The Unocal proportionality doctrine ensuring defensive actions match the threat posed.",
                "distractors": [
                    "The Geneva Convention on the Treatment of Prisoners of War.",
                    "The Magna Carta doctrine of ancient feudal landowner privileges.",
                    "The Admiralty Maritime Law on high-seas oceanic piracy."
                ],
                "explanation_en": "Eleanor cites Delaware Chancery Court requiring proportionality under the Unocal doctrine.",
                "explanation_tr": "Eleanor Delaware mahkemelerinin Unocal doktrini altında orantılılık gösterilmesini şart koştuğunu belirtir."
            },
            {
                "question_en": "What secondary strategic alternative does Cenk suggest pursuing alongside the defensive rights plan?",
                "question_tr_hint": "Cenk savunma hakları planının yanı sıra hangi ikincil stratejik alternatifi takip etmeyi öneriyor?",
                "correct_answer": "Soliciting a white-knight offer from a friendly sovereign wealth fund to secure patient capital.",
                "distractors": [
                    "Filing for voluntary corporate bankruptcy liquidation immediately.",
                    "Firing all company executives and shutting down commercial operations.",
                    "Borrowing hundreds of millions from unverified offshore payday lenders."
                ],
                "explanation_en": "Cenk suggests soliciting a competing bid from a friendly sovereign wealth fund as a white knight.",
                "explanation_tr": "Cenk beyaz şövalye olarak dost bir varlık fonundan rakip teklif almayı önerir."
            },
            {
                "question_en": "At what shareholding milestone did Apex Meridian launch its hostile tender offer?",
                "question_tr_hint": "Apex Meridian düşmanca devralma teklifini hangi hisse sahipliği eşiğinde başlattı?",
                "correct_answer": "Upon crossing the nine percent beneficial ownership threshold.",
                "distractors": [
                    "Upon acquiring ninety-nine percent of total voting stock.",
                    "After purchasing a single solitary common share.",
                    "Without holding any equity ownership in the firm."
                ],
                "explanation_en": "Eleanor states Apex crossed the 9% beneficial ownership threshold.",
                "explanation_tr": "Eleanor Apex'in %9'luk yararlanıcı eşiğini aştığını belirtir."
            }
        ],
        ["work_career", "governance"]
    ),

    # 4. deep-tech-quantum-computing-briefing (technology / physics)
    build_scenario(
        "listening.c2.deep-tech-quantum-computing-briefing",
        "Assessing Fault-Tolerant Topological Quantum Error Correction Milestones",
        "C2", "executive_briefing",
        "Theoretical physicist Dr. Sterling and Quantum Foundry Director Dr. Aslan deliberate surface-code threshold margins, Majorana zero-mode braiding fidelities, and cryostat microwave crosstalk.",
        [
            {"id": "sterling", "name": "Dr. Sterling", "role": "Distinguished Quantum Theorist", "accent": "American"},
            {"id": "aslan", "name": "Dr. Aslan", "role": "Quantum Foundry Engineering Director", "accent": "Turkish"},
        ],
        "c2_deep_tech_quantum_computing_briefing.mp3",
        [
            {"speaker_id": "sterling", "text_en": "Dr. Aslan, our latest millikelvin dilution refrigerator runs exhibit two-qubit gate fidelities hovering at ninety-nine point four percent across our topological surface-code lattice.", "text_tr": "Dr. Aslan, en son milikelvin seyreltme buzdolabı çalışmalarımız, topolojik yüzey kodu kafesimiz boyunca yüzde doksan dokuz virgül dört civarında seyreden iki kübitlik kapı doğrulukları sergiliyor."},
            {"speaker_id": "aslan", "text_en": "That is undeniably impressive, Dr. Sterling, but we remain agonizingly close to the fault-tolerant error threshold of ninety-nine point five percent required to achieve exponential suppression of logical errors.", "text_tr": "Bu inkar edilemez derecede etkileyici Dr. Sterling, ancak mantıksal hataların üstel olarak bastırılmasını sağlamak için gereken yüzde doksan dokuz virgül beşlik hataya dayanıklı hata eşiğine can sıkıcı derecede yakınız."},
            {"speaker_id": "sterling", "text_en": "What is the dominant decoherence mechanism capping our physical gate fidelities below that critical quantum margin?", "text_tr": "Fiziksel kapı doğruluklarımızı bu kritik kuantum marjının altında sınırlayan baskın faz uyumu kaybı mekanizması nedir?"},
            {"speaker_id": "aslan", "text_en": "High-frequency microwave crosstalk between dense coaxial feedlines in the ten-millikelvin mixing chamber stage, compounded by stray infrared photon leakage from the four-kelvin thermal radiation shield.", "text_tr": "On milikelvinlik karıştırma odası aşamasındaki yoğun koaksiyel besleme hatları arasındaki yüksek frekanslı mikrodalga çapraz karışması; buna dört kelvinlik termal radyasyon kalkanından sızan başıboş kızılötesi fotonlar eşlik ediyor."},
            {"speaker_id": "sterling", "text_en": "So quasi-particle poisoning induced by ambient blackbody photons is breaking superconductor Cooper pairs and exciting spurious transitions in our transmon resonators.", "text_tr": "Yani ortamdaki siyah cisim fotonlarının neden olduğu yarı parçacık zehirlenmesi, süper iletken Cooper çiftlerini kırıyor ve transmon rezonatörlerimizde sahte geçişleri uyarıyor."},
            {"speaker_id": "aslan", "text_en": "Precisely. To eliminate that noise floor, we have redesigned the cryostat shrouding with multi-layer high-permeability magnetic shielding and infrared-absorptive eccosorb filters.", "text_tr": "Kesinlikle. Bu gürültü tabanını ortadan kaldırmak için, kriyostat muhafazasını çok katmanlı yüksek geçirgenlikli manyetik koruma ve kızılötesi emici eccosorb filtrelerle yeniden tasarladık."},
            {"speaker_id": "sterling", "text_en": "If that modification pushes our physical gate fidelities past ninety-nine point seven percent, a distance-seven surface code will yield a logical error rate below ten to the minus eight.", "text_tr": "Bu modifikasyon fiziksel kapı doğruluklarımızı yüzde doksan dokuz virgül yediye çıkarırsa, yedi mesafeli bir yüzey kodu onun üzeri eksi sekizin altında bir mantıksal hata oranı sağlayacaktır."},
            {"speaker_id": "aslan", "text_en": "Unlocking the first truly fault-tolerant quantum algorithms capable of simulating complex metalloenzyme catalysts beyond the reach of classical supercomputers.", "text_tr": "Böylece klasik süper bilgisayarların erişemeyeceği karmaşık metaloenzim katalizörlerini simüle edebilen ilk gerçek hataya dayanıklı kuantum algoritmalarının kilidi açılacaktır."},
        ],
        [
            {
                "question_en": "What exact physical gate fidelity threshold is necessary to achieve exponential suppression of logical errors?",
                "question_tr_hint": "Mantıksal hataların üstel olarak bastırılması için tam olarak hangi fiziksel kapı doğruluğu eşiği gereklidir?",
                "correct_answer": "Ninety-nine point five percent (99.5%).",
                "distractors": [
                    "Fifty percent (50.0%).",
                    "One hundred and ten percent (110%).",
                    "Seventy-five point five percent (75.5%)."
                ],
                "explanation_en": "Dr. Aslan specifies the fault-tolerant error threshold is 99.5%.",
                "explanation_tr": "Dr. Aslan hataya dayanıklı eşiğin %99.5 olduğunu belirtir."
            },
            {
                "question_en": "What primary physical noise mechanism capped the qubits' operational performance?",
                "question_tr_hint": "Kübitlerin operasyonel performansını hangi temel fiziksel gürültü mekanizması sınırladı?",
                "correct_answer": "Microwave crosstalk between dense coaxial lines combined with infrared stray photon leakage.",
                "distractors": [
                    "A total failure of municipal electricity supply to the laboratory building.",
                    "A computer virus altering the digital equations stored in word processors.",
                    "Chemical oxidation of wooden laboratory furniture surrounding the cryostat."
                ],
                "explanation_en": "Dr. Aslan explains microwave crosstalk and infrared photon leakage capped fidelities.",
                "explanation_tr": "Dr. Aslan mikrodalga çapraz karışması ve kızılötesi foton sızıntısının performansı kısıtladığını söyler."
            },
            {
                "question_en": "How does ambient blackbody photon leakage degrade superconducting quantum state coherence?",
                "question_tr_hint": "Ortamdaki siyah cisim foton sızıntısı süper iletken kuantum durumu tutarlılığını nasıl bozar?",
                "correct_answer": "It induces quasi-particle poisoning by breaking Cooper pairs, causing spurious energy transitions.",
                "distractors": [
                    "It causes the cryogenic chamber to physically melt into liquid copper.",
                    "It permanently magnetizes all human scientists working in the room.",
                    "It reverses the gravitational pull inside the dilution refrigerator."
                ],
                "explanation_en": "Sterling notes quasi-particle poisoning breaks Cooper pairs and excites spurious transitions.",
                "explanation_tr": "Sterling yarı parçacık zehirlenmesinin Cooper çiftlerini kırıp sahte geçişleri uyardığını açıklar."
            },
            {
                "question_en": "What hardware redesign does Dr. Aslan introduce to suppress the thermal noise floor?",
                "question_tr_hint": "Dr. Aslan termal gürültü tabanını bastırmak için hangi donanım yeniden tasarımını tanıtıyor?",
                "correct_answer": "Cryostat shielding with multi-layer high-permeability magnetic barriers and infrared eccosorb filters.",
                "distractors": [
                    "Wrapping the cryostat in regular commercial household aluminum foil.",
                    "Operating the quantum computer inside an active commercial bakery oven.",
                    "Placing dry ice pellets directly on top of exposed silicon wafer circuits."
                ],
                "explanation_en": "Aslan details cryostat redesign with high-permeability magnetic shielding and eccosorb filters.",
                "explanation_tr": "Aslan çok katmanlı manyetik kalkan ve kızılötesi eccosorb filtreli yeni muhafazayı anlatır."
            },
            {
                "question_en": "What scientific breakthrough becomes computationally tractable once fault-tolerance is unlocked?",
                "question_tr_hint": "Hataya dayanıklılık sağlandığında hangi bilimsel atılım hesaplamalı olarak uygulanabilir hale gelir?",
                "correct_answer": "Simulating complex metalloenzyme biochemical catalysts beyond classical supercomputing limits.",
                "distractors": [
                    "Predicting winning lottery ticket numbers with hundred-percent certainty.",
                    "Designing mechanical internal combustion engines for steam-powered trains.",
                    "Translating modern English books into ancient extinct hieroglyphics."
                ],
                "explanation_en": "Aslan highlights simulating complex metalloenzyme catalysts beyond classical reach.",
                "explanation_tr": "Aslan klasik bilgisayarların erişemediği karmaşık metaloenzim katalizör simülasyonunu vurgular."
            }
        ],
        ["technology", "physics"]
    ),

    # 5. bioethics-germline-gene-editing-treaty (society / ethics)
    build_scenario(
        "listening.c2.bioethics-germline-gene-editing-treaty",
        "Deliberating Global Treaties on Heritable Human Germline Genome Modification",
        "C2", "negotiation",
        "Bioethics Rapporteur Dr. Vance and International Treaty Jurist Zeynep debate human dignity conventions, off-target CRISPR Cas indel risks, and transnational moratorium enforcement sanctions.",
        [
            {"id": "vance", "name": "Dr. Vance", "role": "International Bioethics Rapporteur", "accent": "Canadian"},
            {"id": "zeynep", "name": "Zeynep", "role": "Senior International Law Counsel", "accent": "Turkish"},
        ],
        "c2_bioethics_germline_gene_editing_treaty.mp3",
        [
            {"speaker_id": "vance", "text_en": "Zeynep, the plenary drafting committee is bitterly divided over Article Four of the draft treaty on heritable genomic interventions. Several sovereign delegations are pushing for permissive therapeutic carve-outs.", "text_tr": "Zeynep, genel kurul taslak hazırlama komitesi kalıtsal genomik müdahalelere ilişkin anlaşma taslağının Dördüncü Maddesi konusunda derin bir ayrılık yaşıyor. Birkaç egemen delegasyon izin verici terapötik istisnalar için baskı yapıyor."},
            {"speaker_id": "zeynep", "text_en": "Permissive exceptions for monogenic lethal conditions such as Huntington's or Tay-Sachs sound ethically noble in abstract theory, Dr. Vance, but they establish a slippery slope toward consumer eugenics.", "text_tr": "Huntington veya Tay-Sachs gibi monogenik ölümcül durumlar için izin verici istisnalar soyut teoride etik açıdan asil görünüyor Dr. Vance, ancak tüketici öjeniğine doğru kaygan bir zemin oluşturuyorlar."},
            {"speaker_id": "vance", "text_en": "Yet denying parents the clinical capability to eliminate a fatal heritable pathogenic variant when prime editing offers near-zero off-target indels seems morally untenable to medical ethicists.", "text_tr": "Yine de asal düzenleme neredeyse sıfır hedef dışı delesyon sunarken, ebeveynleri ölümcül kalıtsal patojenik bir varyantı ortadan kaldırma klinik kabiliyetinden mahrum bırakmak tıp etikçilerine ahlaki açıdan savunulamaz görünüyor."},
            {"speaker_id": "zeynep", "text_en": "Near-zero is not zero. Recent somatic cell lineage tracing reveals unintended large-scale chromosomal rearrangements and epigenetic dysregulation that manifest only in subsequent generations.", "text_tr": "Neredeyse sıfır, sıfır demek değildir. Son somatik hücre soy takibi, yalnızca sonraki nesillerde ortaya çıkan istenmeyen büyük ölçekli kromozomal yeniden düzenlemeleri ve epigenetik düzensizlikleri ortaya koyuyor."},
            {"speaker_id": "vance", "text_en": "Beyond molecular biosafety, how do we address the grave geopolitical risk of regulatory arbitrage, where wealthy individuals travel to rogue jurisdictions for cognitive enhancement edits?", "text_tr": "Moleküler biyogüvenliğin ötesinde, varlıklı bireylerin bilişsel geliştirme düzenlemeleri için kuralları hiçe sayan yargı bölgelerine seyahat ettiği düzenleyici arbitrajın ciddi jeopolitik riskini nasıl ele alacağız?"},
            {"speaker_id": "zeynep", "text_en": "We must endow the treaty with universal jurisdiction and extraterritorial criminal sanctions. Any commercial enterprise financing illicit germline alteration must face exclusion from SWIFT and global intellectual property recognition.", "text_tr": "Anlaşmaya evrensel yargı yetkisi ve sınır ötesi cezai yaptırımlar kazandırmalıyız. Yasadışı eşey hattı değişikliğini finanse eden herhangi bir ticari girişim SWIFT'ten ve küresel fikri mülkiyet tanımasından men edilmelidir."},
            {"speaker_id": "vance", "text_en": "Coupled with an absolute ten-year binding moratorium on in-vivo reproductive implantation pending multi-generational primate longitudinal safety trials.", "text_tr": "Çok nesilli primat uzunlamasına güvenlik denemeleri sonuçlanana kadar, canlı içi üreme implantasyonu üzerinde mutlak on yıllık bağlayıcı bir moratoryum ile birlikte."},
            {"speaker_id": "zeynep", "text_en": "Agreed. That preserves scientific research on somatic therapies while fortifying humanity's shared genomic commons against commercial commodification.", "text_tr": "Anlaştık. Bu, somatik tedavilere yönelik bilimsel araştırmaları korurken, insanlığın ortak genomik mirasını ticari metalaşmaya karşı güçlendirir."},
        ],
        [
            {
                "question_en": "What primary ethical danger does Zeynep emphasize regarding therapeutic gene-editing carve-outs?",
                "question_tr_hint": "Zeynep terapötik gen düzenleme istisnalarına ilişkin hangi temel etik tehlikeyi vurguluyor?",
                "correct_answer": "They establish an irreversible slippery slope transitioning from rare disease cures toward consumer eugenics.",
                "distractors": [
                    "They cause human DNA to convert into plant cellular structures.",
                    "They permanently bankrupt international pharmaceutical research charities.",
                    "They eliminate all known infectious diseases from the planet simultaneously."
                ],
                "explanation_en": "Zeynep warns permissive exceptions establish a slippery slope toward consumer eugenics.",
                "explanation_tr": "Zeynep istisnaların tüketici öjeniğine doğru kaygan bir zemin oluşturduğu uyarısında bulunur."
            },
            {
                "question_en": "What biological risk does Zeynep identify despite claimed near-zero off-target editing rates?",
                "question_tr_hint": "Zeynep hedeften sapma oranlarının düşüklüğüne rağmen hangi biyolojik riski tespit ediyor?",
                "correct_answer": "Large-scale chromosomal rearrangements and epigenetic dysregulation manifesting in later generations.",
                "distractors": [
                    "Patients growing physical metallic armor over their skeletal joints.",
                    "The immediate instantaneous loss of human speech capability.",
                    "Spontaneous nuclear fission within red blood cell mitochondria."
                ],
                "explanation_en": "Zeynep notes large-scale chromosomal rearrangements that appear in subsequent generations.",
                "explanation_tr": "Zeynep sonraki nesillerde ortaya çıkan kromozom yeniden düzenlenmelerini ve epigenetik riskleri belirtir."
            },
            {
                "question_en": "How does Zeynep propose neutralizing the threat of transnational regulatory arbitrage?",
                "question_tr_hint": "Zeynep ulusötesi düzenleyici arbitraj tehdidini nasıl etkisiz hale getirmeyi öneriyor?",
                "correct_answer": "By establishing universal extraterritorial jurisdiction with SWIFT exclusions and IP invalidation.",
                "distractors": [
                    "By constructing a physical wall around nations performing biotechnology.",
                    "By eliminating all international passport travel for scientific researchers.",
                    "By offering billions of dollars in unconditional subsidies to rogue clinics."
                ],
                "explanation_en": "Zeynep advocates universal jurisdiction, SWIFT banking exclusion, and IP revocation.",
                "explanation_tr": "Zeynep evrensel yargı, SWIFT'ten men ve fikri mülkiyet haklarının iptalini önerir."
            },
            {
                "question_en": "What moratorium timeframe does Dr. Vance incorporate into the proposed draft agreement?",
                "question_tr_hint": "Dr. Vance önerilen anlaşma taslağına hangi moratoryum süresini dahil ediyor?",
                "correct_answer": "An absolute ten-year binding moratorium on reproductive in-vivo implantation.",
                "distractors": [
                    "A brief forty-eight-hour pause over an upcoming weekend.",
                    "A permanent ban on all medical healthcare procedures forever.",
                    "A ninety-day non-binding voluntary recommendation."
                ],
                "explanation_en": "Dr. Vance specifies an absolute ten-year binding moratorium on reproductive implantation.",
                "explanation_tr": "Dr. Vance üreme implantasyonu üzerinde 10 yıllık bağlayıcı moratoryum belirtir."
            },
            {
                "question_en": "What category of gene editing remains explicitly safeguarded and permitted under the compromise?",
                "question_tr_hint": "Uzlaşma kapsamında hangi gen düzenleme kategorisi açıkça korunmakta ve izin verilmektedir?",
                "correct_answer": "Non-heritable somatic cell therapies addressing diseases within individual patients.",
                "distractors": [
                    "Enhancement editing to double human physical athletic strength.",
                    "Cosmetic gene alterations modifying human eye and hair pigments.",
                    "Cloning human individuals for commercial organ harvesting."
                ],
                "explanation_en": "Zeynep notes the treaty preserves scientific research on somatic therapies.",
                "explanation_tr": "Zeynep somatik tedavilere yönelik bilimsel araştırmaların korunduğunu teyit eder."
            }
        ],
        ["society", "ethics"]
    ),

    # 6. cross-cultural-linguistic-diplomacy (culture / communication)
    build_scenario(
        "listening.c2.cross-cultural-linguistic-diplomacy",
        "Deconstructing Cross-Cultural Nuance and Pragmatic Presupposition in Treaties",
        "C2", "stakeholder_alignment",
        "Linguistic diplomat Alistair and Senior Polyglot Negotiator Defne dissect subtle semantic divergence, performative modality, and non-verbal pragmatics in multilateral treaty draftsmanship.",
        [
            {"id": "alistair", "name": "Alistair", "role": "Senior Diplomatic Linguist", "accent": "British"},
            {"id": "defne", "name": "Defne", "role": "Multilateral Treaty Negotiator", "accent": "Turkish"},
        ],
        "c2_cross_cultural_linguistic_diplomacy.mp3",
        [
            {"speaker_id": "alistair", "text_en": "Defne, we have spent forty hours scrutinizing the bilingual French and English treaty texts. Yet I remain deeply uneasy with how the Russian and Chinese delegations are translating the deontic modal 'shall'.", "text_tr": "Defne, iki dilli Fransızca ve İngilizce anlaşma metinlerini incelemek için kırk saat harcadık. Yine de Rus ve Çin delegasyonlarının deontik zorunluluk kipi olan 'shall' ifadesini nasıl çevirdikleri konusunda derin bir tedirginlik duyuyorum."},
            {"speaker_id": "defne", "text_en": "Your unease is well-founded, Alistair. In Anglo-American common law jurisprudence, 'shall' imposes a strictly non-negotiable imperative legal obligation.", "text_tr": "Tedirginliğin son derece haklı Alistair. Anglo-Amerikan teamül hukuku içtihadında 'shall', kesinlikle müzakere edilemez zorunlu bir yasal yükümlülük getirir."},
            {"speaker_id": "alistair", "text_en": "Precisely. Whereas in civil law codifications and Sinophone diplomatic pragmatics, equivalent modal particles are frequently interpreted as aspirational intent rather than actionable mandate.", "text_tr": "Kesinlikle. Oysa kıta Avrupası hukuk kodifikasyonlarında ve Çince diplomatik pragmatiğinde, eşdeğer modal parçacıklar sıklıkla uygulanabilir bir talimattan ziyade temenni niteliğinde bir niyet olarak yorumlanır."},
            {"speaker_id": "defne", "text_en": "We observed an identical divergence in Security Council Resolution 242 regarding the omission of the definite article 'the' before 'territories', creating decades of interpretive stalemate.", "text_tr": "Güvenlik Konseyi'nin 242 sayılı Kararında, 'topraklar' ifadesinden önce 'the' belirli tanımlığının çıkarılması konusunda on yıllar süren yorumsal çıkmaza yol açan birebir aynı farklılığı gözlemledik."},
            {"speaker_id": "alistair", "text_en": "Constructive ambiguity has its tactical merits during fragile plenary negotiations, but in a non-proliferation pact, ambiguity invites catastrophic miscalculation.", "text_tr": "Yapıcı muğlaklığın kırılgan genel kurul müzakerelerinde taktiksel faydaları vardır, ancak yayılmanın önlenmesi paktında muğlaklık felaket niteliğinde yanlış hesaplamalara davetiye çıkarır."},
            {"speaker_id": "defne", "text_en": "To eliminate interpretive divergence, we must insist on an authoritative authenticating protocol. We must annex an explicit interpretative appendix defining the performative force of every operative verb.", "text_tr": "Yorumsal farklılıkları ortadan kaldırmak için yetkili bir onaylama protokolünde ısrar etmeliyiz. Her eylemsel fiilin edimsel gücünü tanımlayan açık bir yorumsal ek iliştirmeliyiz."},
            {"speaker_id": "alistair", "text_en": "Stipulating that 'undertakes to' conveys identical binding force to 'shall', while 'strives to' denotes non-justiciable aspirational endeavor across all six official languages.", "text_tr": "Tüm altı resmi dilde 'undertakes to' ifadesinin 'shall' ile özdeş bağlayıcı güç taşıdığını, 'strives to' ifadesinin ise dava konusu yapılamaz temenni niteliğinde bir çabayı belirttiğini şart koşarak."},
            {"speaker_id": "defne", "text_en": "Exactly. Linguistic precision is the bedrock of enduring multilateral treaties; without it, peace rests on shifting tectonic sands.", "text_tr": "Kesinlikle. Dilbilimsel kesinlik, kalıcı çok taraflı anlaşmaların temel taşıdır; o olmadan barış, kaygan tektonik kumlar üzerinde durur."},
        ],
        [
            {
                "question_en": "What linguistic divergence in legal interpretation troubles the diplomatic negotiators?",
                "question_tr_hint": "Diplomatik müzakerecileri yasal yorumlamada hangi dilbilimsel farklılık endişelendiriyor?",
                "correct_answer": "The modal 'shall' denotes a strict imperative in common law but may be viewed as aspirational in other traditions.",
                "distractors": [
                    "The font size of the English draft is smaller than the French text.",
                    "Translators accidentally swapped all personal pronouns with animal names.",
                    "The treaty was drafted in rhyming poetry rather than prose."
                ],
                "explanation_en": "Alistair and Defne discuss how 'shall' is imperative in common law but read as aspirational elsewhere.",
                "explanation_tr": "Alistair ve Defne 'shall' kipinin teamül hukukunda kesin zorunluluk, başka geleneklerde ise temenni sayılabildiğini açıklar."
            },
            {
                "question_en": "What historical diplomatic precedent does Defne cite to illustrate ambiguous treaty drafting?",
                "question_tr_hint": "Defne muğlak anlaşma yazımını örneklendirmek için hangi tarihi diplomatik emsali öne sürüyor?",
                "correct_answer": "UN Security Council Resolution 242 and the contested omission of the definite article 'the'.",
                "distractors": [
                    "The Treaty of Versailles signing banquet menu translation.",
                    "The Louisiana Purchase payment transaction currency receipt.",
                    "The 1945 Yalta Conference seating arrangement dispute."
                ],
                "explanation_en": "Defne references UN Resolution 242 and the omitted 'the' creating decades of stalemate.",
                "explanation_tr": "Defne 242 sayılı karardaki 'the' tanımlığının çıkarılmasının yarattığı yorumsal tıkanıklığı hatırlatır."
            },
            {
                "question_en": "Why does Alistair argue against relying on 'constructive ambiguity' in this treaty?",
                "question_tr_hint": "Alistair bu anlaşmada 'yapıcı muğlaklığa' güvenilmesine neden karşı çıkıyor?",
                "correct_answer": "In a nuclear non-proliferation pact, ambiguity creates the risk of catastrophic strategic miscalculation.",
                "distractors": [
                    "Constructive ambiguity is strictly prohibited by international grammar laws.",
                    "Ambiguity requires hiring millions of additional bilingual proofreaders.",
                    "Ambiguity forces both governments to abolish their diplomatic corps."
                ],
                "explanation_en": "Alistair notes that in a non-proliferation pact, ambiguity invites catastrophic miscalculation.",
                "explanation_tr": "Alistair yayılmayı önleme paktında muğlaklığın felaket boyutunda yanlış hesaplamalara yol açacağını belirtir."
            },
            {
                "question_en": "What concrete drafting remedy does Defne propose to resolve semantic discrepancies?",
                "question_tr_hint": "Defne anlamsal tutarsızlıkları gidermek için hangi somut yazım çözümünü öneriyor?",
                "correct_answer": "Annexing an explicit interpretative appendix defining the performative force of every operative verb.",
                "distractors": [
                    "Replacing all treaty words with universal graphic emoji symbols.",
                    "Burning the draft treaty and conducting all international relations orally.",
                    "Permitting each state to write its own private secret treaty version."
                ],
                "explanation_en": "Defne proposes annexing an interpretative appendix defining the performative force of operative verbs.",
                "explanation_tr": "Defne her fiilin edimsel gücünü tanımlayan açık bir yorumsal ek iliştirilmesini önerir."
            },
            {
                "question_en": "Across how many official diplomatic languages must this terminological precision be synchronized?",
                "question_tr_hint": "Bu terminolojik kesinlik kaç resmi diplomatik dil arasında senkronize edilmelidir?",
                "correct_answer": "All six official languages of the multilateral forum.",
                "distractors": [
                    "Only one single language chosen by competitive debate.",
                    "Over three thousand regional indigenous dialects.",
                    "Exactly two national languages."
                ],
                "explanation_en": "Alistair refers to standardizing performative force across all six official languages.",
                "explanation_tr": "Alistair tüm altı resmi dilde bu standartlaştırmanın sağlanması gerektiğini ifade eder."
            }
        ],
        ["culture", "communication"]
    ),

    # 7. catastrophic-grid-blackstart-restoration (problem-solving / infrastructure)
    build_scenario(
        "listening.c2.catastrophic-grid-blackstart-restoration",
        "Executing Synchronous Islanding and Continental Blackstart Restoration",
        "C2", "incident_response",
        "Transmission System Coordinator Bruce and Chief Power Grid Controller Tolga orchestrate synchronous condenser balancing and cranking path energization following a cascading continental grid collapse.",
        [
            {"id": "bruce", "name": "Bruce", "role": "Chief Transmission Grid Director", "accent": "Australian"},
            {"id": "tolga", "name": "Tolga", "role": "National Power Control Coordinator", "accent": "Turkish"},
        ],
        "c2_catastrophic_grid_blackstart_restoration.mp3",
        [
            {"speaker_id": "bruce", "text_en": "Tolga, we have total systemic blackout across the southeastern transmission corridor. Four nuclear reactors and six gigawatts of combined-cycle turbines tripped offline on under-frequency protection.", "text_tr": "Tolga, güneydoğu iletim koridorunda topyekûn sistemsel kesinti yaşıyoruz. Dört nükleer reaktör ve altı gigavatlık kombine çevrim türbini düşük frekans korumasıyla devre dışı kaldı."},
            {"speaker_id": "tolga", "text_en": "Copy that, Bruce. Our national dispatch center is running on emergency diesel generators. System frequency plunged through forty-seven point five hertz before the interties severed.", "text_tr": "Anlaşıldı Bruce. Ulusal sevk merkezimiz acil durum dizel jeneratörleriyle çalışıyor. Bağlantı hatları kopmadan önce sistem frekansı kırk yedi virgül beş hertzin altına indi."},
            {"speaker_id": "bruce", "text_en": "We need to initiate blackstart protocol Delta-Three immediately. Do we have autonomous cranking power at the Keban hydroelectric generating station?", "text_tr": "Derhal Delta-Üç kara başlatma protokolünü başlatmamız gerekiyor. Keban hidroelektrik üretim santralinde otonom çevirme gücümüz var mı?"},
            {"speaker_id": "tolga", "text_en": "Affirmative. Unit four at Keban has blackstart diesel capability and has just established local station service voltage. We are ready to energize the primary four-hundred-kilovolt transmission spine.", "text_tr": "Olumlu. Keban'daki dördüncü ünite kara başlatma dizel kabiliyetine sahip ve az önce yerel santral servis voltajını sağladı. Ana dört yüz kilovolt luk iletim omurgasına enerji vermeye hazırız."},
            {"speaker_id": "bruce", "text_en": "Be extremely wary of the Ferranti effect, Tolga. Energizing unloaded high-voltage lines over hundreds of kilometers causes capacitive reactive power surges that can spike terminal voltages beyond insulation breakdown limits.", "text_tr": "Ferranti etkisine karşı son derece dikkatli ol Tolga. Yüzlerce kilometre boyunca yüksüz yüksek gerilim hatlarına enerji vermek, uç gerilimlerini yalıtım delinme sınırlarının ötesine fırlatabilen kapasitif reaktif güç dalgalanmalarına neden olur."},
            {"speaker_id": "tolga", "text_en": "Understood. We are switching in our three-hundred-megavar shunt reactors at the intermediate substations to absorb reactive charging current before we close the breaker.", "text_tr": "Anlaşıldı. Kesiciyi kapatmadan önce reaktif şarj akımını emmek için ara trafo merkezlerindeki üç yüz megavarlık paralel şönt reaktörlerimizi devreye alıyoruz."},
            {"speaker_id": "bruce", "text_en": "Once that transmission path is energized, we can supply cranking power to the gas turbines at Ambarlı, forming our first stable synchronized island.", "text_tr": "Bu iletim yolu enerjilendiğinde, ilk kararlı senkronize adamızı oluşturarak Ambarlı'daki gaz türbinlerine çevirme gücü sağlayabiliriz."},
            {"speaker_id": "tolga", "text_en": "Synchronous island established. We are picking up block industrial loads incrementally to dampen frequency oscillation.", "text_tr": "Senkronize ada kuruldu. Frekans salınımını sönümlemek için kademeli olarak blok endüstriyel yükleri devreye alıyoruz."},
        ],
        [
            {
                "question_en": "What initiated the catastrophic regional electrical blackout across the transmission network?",
                "question_tr_hint": "İletim ağı genelinde felaket niteliğindeki bölgesel elektrik kesintisini ne başlattı?",
                "correct_answer": "Under-frequency protective trips disconnecting major nuclear and gas turbine generating plants.",
                "distractors": [
                    "A massive lightning bolt striking every home refrigerator in the country.",
                    "An intentional decision to turn off electricity for annual Earth Hour.",
                    "An unprecedented heatwave causing solar panels to freeze into solid ice."
                ],
                "explanation_en": "Bruce reports reactors and turbines tripped offline on under-frequency protection.",
                "explanation_tr": "Bruce nükleer reaktörler ve türbinlerin düşük frekans korumasıyla devre dışı kaldığını bildirir."
            },
            {
                "question_en": "What critical operational danger does Bruce caution against when energizing unloaded long-distance lines?",
                "question_tr_hint": "Bruce yüksüz uzun mesafeli hatlara enerji verirken hangi kritik operasyonel tehlikeye karşı uyarıyor?",
                "correct_answer": "The Ferranti effect creating capacitive voltage spikes that exceed line insulation tolerances.",
                "distractors": [
                    "Transformer oil turning into flammable gasoline.",
                    "Overhead copper cables physically dissolving in the wind.",
                    "Lightning bugs nesting inside high-voltage circuit breakers."
                ],
                "explanation_en": "Bruce warns that the Ferranti effect causes capacitive surges that spike voltages beyond breakdown limits.",
                "explanation_tr": "Bruce Ferranti etkisinin yalıtım sınırlarını aşan kapasitif gerilim sıçramalarına yol açacağını söyler."
            },
            {
                "question_en": "How does Tolga counteract capacitive reactive power surges along the 400kV line?",
                "question_tr_hint": "Tolga 400kV hattı boyunca kapasitif reaktif güç dalgalanmalarını nasıl dengeliyor?",
                "correct_answer": "By switching in 300-megavar shunt reactors at intermediate substations to absorb reactive charge.",
                "distractors": [
                    "By opening fire hydrants to cool down the substation control rooms.",
                    "By asking consumers to turn on their vacuum cleaners simultaneously.",
                    "By shutting down diesel backup generators completely."
                ],
                "explanation_en": "Tolga explains they are switching in 300-megavar shunt reactors to absorb reactive current.",
                "explanation_tr": "Tolga reaktif akımı emmek için 300 megavarlık şönt reaktörleri devreye aldıklarını açıklar."
            },
            {
                "question_en": "Which power generating station provides autonomous blackstart capability in the restoration plan?",
                "question_tr_hint": "Restorasyon planında hangi elektrik santrali otonom kara başlatma kabiliyeti sağlamaktadır?",
                "correct_answer": "Unit four at the Keban hydroelectric generating facility equipped with diesel cranking.",
                "distractors": [
                    "A small rooftop residential solar array in suburban Ankara.",
                    "An abandoned nineteenth-century coal locomotive boiler.",
                    "A wind farm consisting of three decorative miniature garden windmills."
                ],
                "explanation_en": "Tolga confirms Keban unit four has blackstart diesel capability and established service voltage.",
                "explanation_tr": "Tolga Keban'daki 4. ünitenin kara başlatma dizel kabiliyetine sahip olduğunu doğrular."
            },
            {
                "question_en": "How do the controllers stabilize grid frequency once the synchronous island is formed?",
                "question_tr_hint": "Kontrolörler senkronize ada oluştuktan sonra şebeke frekansını nasıl stabilize ediyor?",
                "correct_answer": "By picking up block industrial loads incrementally to dampen dynamic frequency oscillations.",
                "distractors": [
                    "By immediately connecting the entire nationwide domestic consumer load at once.",
                    "By disconnecting all generators from the electrical grid permanently.",
                    "By converting alternating electrical current into direct battery storage."
                ],
                "explanation_en": "Tolga states they are picking up block industrial loads incrementally to dampen oscillation.",
                "explanation_tr": "Tolga salınımları sönümlemek için blok endüstriyel yüklerin kademeli alındığını belirtir."
            }
        ],
        ["problem_solving", "infrastructure"]
    ),

    # 8. antitrust-algorithmic-monopoly-hearing (communication / legal)
    build_scenario(
        "listening.c2.antitrust-algorithmic-monopoly-hearing",
        "Antitrust Adjudication of Algorithmic Collusion and Monopsony Power",
        "C2", "negotiation",
        "Competition Commissioner Marianne and Antitrust Economic Counsel Can dissect tacit algorithmic pricing coordination, programmatic bid shading, and platform market foreclosure.",
        [
            {"id": "marianne", "name": "Commissioner Marianne", "role": "Senior Competition Commissioner", "accent": "American"},
            {"id": "can", "name": "Can", "role": "Chief Antitrust Economic Counsel", "accent": "Turkish"},
        ],
        "c2_antitrust_algorithmic_monopoly_hearing.mp3",
        [
            {"speaker_id": "marianne", "text_en": "Counselor Can, our antitrust investigative committee has concluded its deposition of senior software engineers from the dominant online rental housing platform. Have we established unlawful price-fixing under Section One of the Sherman Act?", "text_tr": "Avukat Can, antitröst soruşturma komitemiz baskın çevrimiçi kiralık konut platformunun kıdemli yazılım mühendislerinin ifadelerini tamamladı. Sherman Yasası'nın Birinci Maddesi uyarınca yasadışı fiyat tespitini kanıtlayabildik mi?"},
            {"speaker_id": "can", "text_en": "The evidentiary challenge, Commissioner Marianne, is that the property management companies never convened in a smoky room to exchange express horizontal pricing agreements.", "text_tr": "Delil niteliğindeki zorluk Komisyon Üyesi Marianne, mülk yönetim şirketlerinin açık yatay fiyatlandırma anlaşmaları yapmak için hiçbir zaman dumanlı bir odada bir araya gelmemiş olmasıdır."},
            {"speaker_id": "marianne", "text_en": "Instead, they all outsourced algorithmic pricing discretion to a common third-party software vendor that pools proprietary lease occupancy data across competitor portfolios.", "text_tr": "Bunun yerine hepsi, rakip portföyler arasındaki tescilli kira doluluk verilerini havuzda toplayan ortak bir üçüncü taraf yazılım satıcısına algoritmik fiyatlandırma takdirini devrettiler."},
            {"speaker_id": "can", "text_en": "Precisely. Under traditional antitrust doctrine, conscious parallelism without an express agreement was legally protected. But here, the algorithm functions as a hub-and-spoke algorithmic cartel.", "text_tr": "Kesinlikle. Geleneksel antitröst doktrininde, açık bir anlaşma olmaksızın bilinçli paralellik yasal olarak korunuyordu. Ancak burada algoritma, bir merkez-ve-jant algoritmik karteli işlevi görüyor."},
            {"speaker_id": "marianne", "text_en": "By delegating revenue-maximization algorithms to a single intermediary, competing landlords tacitly agree to restrict housing inventory and artificially elevate residential rents nationwide.", "text_tr": "Gelir maksimizasyonu algoritmalarını tek bir aracıya devrederek, rakip ev sahipleri konut envanterini kısıtlama ve ülke çapında konut kiralarını yapay olarak yükseltme konusunda zımnen anlaşıyorlar."},
            {"speaker_id": "can", "text_en": "Furthermore, our econometric regressions prove that landlords who rejected the algorithm's non-negotiable pricing recommendations were algorithmically deprioritized in search ranking results.", "text_tr": "Dahası, ekonometrik regresyonlarımız, algoritmanın müzakereye kapalı fiyat tavsiyelerini reddeden ev sahiplerinin arama sıralaması sonuçlarında algoritmik olarak geri plana itildiğini kanıtlıyor."},
            {"speaker_id": "marianne", "text_en": "Which establishes coercive enforcement and anti-competitive platform foreclosure beyond mere passive market coordination.", "text_tr": "Bu da yalnızca pasif pazar koordinasyonunun ötesinde baskıcı yaptırımı ve rekabeti engelleyici platform dışlamasını kanıtlar."},
            {"speaker_id": "can", "text_en": "We recommend filing a civil enforcement injunction demanding structural divestiture of the algorithmic engine and prohibiting competitor data pooling across metropolitan statistical areas.", "text_tr": "Algoritmik motorun yapısal olarak elden çıkarılmasını talep eden ve metropol istatistiksel alanları genelinde rakip veri havuzunu yasaklayan bir hukuk davası ihtiyati tedbiri açılmasını öneriyoruz."},
        ],
        [
            {
                "question_en": "What novel evidentiary hurdle distinguishes this case from traditional antitrust conspiracies?",
                "question_tr_hint": "Bu davayı geleneksel antitröst komplolarından hangi yeni delil engeli ayırıyor?",
                "correct_answer": "Landlords never met or entered express horizontal agreements, delegating pricing to a shared algorithm.",
                "distractors": [
                    "All rental contracts were signed using invisible ultraviolet ink.",
                    "The housing platform operated exclusively outside planetary legal jurisdiction.",
                    "No financial transactions took place in modern national currency."
                ],
                "explanation_en": "Can explains property managers never met to form express agreements, using a shared algorithm instead.",
                "explanation_tr": "Can şirketlerin asla doğrudan toplanmadığını, fiyatlamayı ortak bir algoritmaya devrettiğini belirtir."
            },
            {
                "question_en": "Under what legal concept does Can classify the algorithmic pricing scheme?",
                "question_tr_hint": "Can algoritmik fiyatlandırma düzenini hangi yasal kavram altında sınıflandırıyor?",
                "correct_answer": "A hub-and-spoke algorithmic cartel where a common software vendor coordinates market pricing.",
                "distractors": [
                    "A benign consumer discount coupon loyalty program.",
                    "An involuntary charitable donation program for underprivileged tenants.",
                    "A state-sponsored municipal public housing initiative."
                ],
                "explanation_en": "Can argues the algorithm functions as a hub-and-spoke algorithmic cartel.",
                "explanation_tr": "Can algoritmanın bir merkez-ve-jant karteli işlevi gördüğünü savunur."
            },
            {
                "question_en": "What retaliatory mechanism punished landlords who deviated from the algorithm's recommendations?",
                "question_tr_hint": "Algoritmanın tavsiyelerinden sapan ev sahiplerini hangi misilleme mekanizması cezalandırdı?",
                "correct_answer": "Their listings were algorithmically suppressed and deprioritized in tenant search results.",
                "distractors": [
                    "The software vendor dispatched private armed mercenaries to seize apartment buildings.",
                    "Landlords were immediately sentenced to mandatory federal prison sentences.",
                    "The platform deleted all customer reviews from the internet entirely."
                ],
                "explanation_en": "Can notes landlords rejecting recommendations were algorithmically deprioritized in search results.",
                "explanation_tr": "Can tavsiyeleri reddedenlerin arama sıralamasında geriye itildiğini ekonometrik olarak kanıtladıklarını söyler."
            },
            {
                "question_en": "What ultimate remedy does Counsel Can recommend to the Competition Commission?",
                "question_tr_hint": "Avukat Can Rekabet Komisyonu'na hangi nihai çözümü tavsiye ediyor?",
                "correct_answer": "Structural divestiture of the algorithmic pricing engine and a ban on pooling competitor data.",
                "distractors": [
                    "Subsidizing the software company with fifty billion dollars in taxpayer grants.",
                    "Ordering every apartment building in the country to be demolished.",
                    "A minor written apology published on the platform's social media page."
                ],
                "explanation_en": "Can recommends seeking structural divestiture of the algorithm and banning competitor data pooling.",
                "explanation_tr": "Can algoritmanın elden çıkarılması ve rakip veri havuzlarının yasaklanmasını önerir."
            },
            {
                "question_en": "Under what foundational statute is the antitrust enforcement action being brought?",
                "question_tr_hint": "Antitröst icra davası hangi temel yasa kapsamında açılmaktadır?",
                "correct_answer": "Section One of the Sherman Act.",
                "distractors": [
                    "The Federal Maritime Piracy Act of 1819.",
                    "The National Environmental Wilderness Preservation Act.",
                    "The Geneva Convention on Civil Aviation Protocol."
                ],
                "explanation_en": "Marianne refers to establishing price-fixing under Section One of the Sherman Act.",
                "explanation_tr": "Marianne Sherman Yasası'nın 1. Maddesi kapsamındaki fiyat tespitine atıfta bulunur."
            }
        ],
        ["communication", "legal"]
    ),

    # 9. critical-infrastructure-zero-day-incident (problem-solving / cybersecurity)
    build_scenario(
        "listening.c2.critical-infrastructure-zero-day-incident",
        "Responding to Nation-State Kinetic SCADA Zero-Day Interdiction",
        "C2", "incident_response",
        "National Cyber Defense Chief Evelyn and Industrial Cybersecurity Director Sinan isolate air-gapped turbine telemetry networks from rootkit firmware manipulation during an active nation-state offensive.",
        [
            {"id": "evelyn", "name": "Director Evelyn", "role": "National Cyber Defense Director", "accent": "American"},
            {"id": "sinan", "name": "Commander Sinan", "role": "Industrial Cyber Infrastructure Commander", "accent": "Turkish"},
        ],
        "c2_critical_infrastructure_zero_day_incident.mp3",
        [
            {"speaker_id": "evelyn", "text_en": "Commander Sinan, situational awareness reports indicate an active cyber-kinetic offensive targeting our trans-Anatolian natural gas pipeline compression stations.", "text_tr": "Komutan Sinan, durumsal farkındalık raporları Trans-Anadolu doğal gaz boru hattı kompresör istasyonlarımızı hedef alan aktif bir siber-kinetik taarruza işaret ediyor."},
            {"speaker_id": "sinan", "text_en": "Director Evelyn, telemetry alarms at Station Four triggered three minutes ago. Centrifugal turbine RPM tachometers are displaying severe harmonic oscillations.", "text_tr": "Direktör Evelyn, Dördüncü İstasyondaki telemetri alarmları üç dakika önce devreye girdi. Santrifüj türbin devir takometreleri şiddetli harmonik salınımlar gösteriyor."},
            {"speaker_id": "evelyn", "text_en": "Is this a physical mechanical bearing failure, or has a hostile nation-state weaponized a zero-day vulnerability in our programmable logic controllers?", "text_tr": "Bu fiziksel bir mekanik yatak arızası mı, yoksa düşman bir ulus-devlet programlanabilir mantıksal denetleyicilerimizdeki bir sıfır gün açığını silaha mı dönüştürdü?"},
            {"speaker_id": "sinan", "text_en": "Forensics confirm malicious firmware injection into our safety instrumented systems. The threat actor flashed weaponized ladder logic that overrides mechanical overspeed shutoff valves.", "text_tr": "Adli bilişim, güvenlik enstrümanlı sistemlerimize kötü amaçlı bellenim enjeksiyonunu doğruluyor. Tehdit aktörü, mekanik aşırı hız kapatma vanalarını geçersiz kılan silahlandırılmış merdiven mantığı yüklemiş."},
            {"speaker_id": "evelyn", "text_en": "They are attempting to force catastrophic physical rupture through acoustic resonance, mimicking the Stuxnet centrifuge playbook.", "text_tr": "Stuxnet santrifüj taktiğini taklit ederek, akustik rezonans yoluyla yıkıcı bir fiziksel patlamayı zorlamaya çalışıyorlar."},
            {"speaker_id": "sinan", "text_en": "Precisely. The malicious rootkit also spoofed our human-machine interface consoles with replayed loop recordings showing benign green telemetry.", "text_tr": "Kesinlikle. Kötü amaçlı kök kiti ayrıca insan-makine arayüzü konsollarımızı zararsız yeşil telemetri gösteren tekrarlanan döngü kayıtlarıyla kandırdı."},
            {"speaker_id": "evelyn", "text_en": "Sever the optical fiber uplinks immediately. Authorize the tactical air-gap severance protocol!", "text_tr": "Optik fiber bağlantılarını derhal kesin. Taktiksel hava boşluğu koparma protokolünü yetkilendirin!"},
            {"speaker_id": "sinan", "text_en": "Air-gap severed. We have manually tripped the analog pneumatic emergency blowdown valves on-site, venting pressure safely to flare stacks and preventing pipeline rupture.", "text_tr": "Hava boşluğu sağlandı. Sahadaki analog pnömatik acil durum tahliye vanalarını manuel olarak açtık, basıncı güvenli bir şekilde meşale bacalarına tahliye ettik ve boru hattı patlamasını önledik."},
        ],
        [
            {
                "question_en": "What malicious technique did the threat actor employ to disable industrial safety protections?",
                "question_tr_hint": "Tehdit aktörü endüstriyel güvenlik korumalarını devre dışı bırakmak için hangi kötü amaçlı tekniği kullandı?",
                "correct_answer": "Flashing weaponized ladder logic firmware to override emergency mechanical overspeed valves.",
                "distractors": [
                    "Cutting power cords with manual wire clippers in the reception area.",
                    "Sending deceptive phishing emails promising free vacation airline tickets.",
                    "Unscrewing physical pressure meters using hand-held socket wrenches."
                ],
                "explanation_en": "Sinan confirms malicious firmware flashed weaponized ladder logic overriding overspeed shutoffs.",
                "explanation_tr": "Sinan kötü niyetli yazılımın vanaları geçersiz kılan silahlandırılmış merdiven mantığı yüklediğini açıklar."
            },
            {
                "question_en": "How did the adversary deceive plant operators viewing human-machine interface (HMI) screens?",
                "question_tr_hint": "Saldırgan insan-makine arayüzü (HMI) ekranlarına bakan tesis operatörlerini nasıl kandırdı?",
                "correct_answer": "By replaying looped sensor telemetry showing benign operational conditions.",
                "distractors": [
                    "By physically turning off computer monitor power buttons.",
                    "By projecting animated cartoon holograms inside the control room.",
                    "By replacing computer monitors with static printed oil paintings."
                ],
                "explanation_en": "Sinan reports the rootkit spoofed HMIs with replayed loop recordings showing benign green telemetry.",
                "explanation_tr": "Sinan kök kitinin yeşil telemetri döngüsü oynatarak ekranları sahte şekilde normal gösterdiğini belirtir."
            },
            {
                "question_en": "What historic cyber-kinetic precedent does Director Evelyn reference during the crisis?",
                "question_tr_hint": "Direktör Evelyn kriz sırasında hangi tarihi siber-kinetik emsale atıfta bulunuyor?",
                "correct_answer": "The Stuxnet operation which targeted uranium enrichment centrifuges with resonant destruction.",
                "distractors": [
                    "The nineteen-eighty-eight Morris internet worm disruption.",
                    "The Sony Pictures entertainment data leak incident.",
                    "The Millennium Y2K calendar transition preparations."
                ],
                "explanation_en": "Evelyn compares the attack to the Stuxnet centrifuge playbook causing acoustic resonance rupture.",
                "explanation_tr": "Evelyn saldırıyı akustik rezonansla parçalamayı hedefleyen Stuxnet taktiğine benzetir."
            },
            {
                "question_en": "What immediate physical containment action did Commander Sinan execute to prevent catastrophe?",
                "question_tr_hint": "Komutan Sinan felaketi önlemek için hangi acil fiziksel kontrol eylemini gerçekleştirdi?",
                "correct_answer": "Severing network fiber links and manually actuating analog pneumatic emergency blowdown valves.",
                "distractors": [
                    "Calling the pipeline manufacturer customer support telephone line.",
                    "Evacuating all human populations from the continent immediately.",
                    "Dousing server racks with large buckets of tap water."
                ],
                "explanation_en": "Sinan confirms severing the air gap and manually tripping pneumatic blowdown valves to vent pressure.",
                "explanation_tr": "Sinan hava boşluğu bağlantısını kesip analog vanaları açarak basıncı güvenle tahliye ettiklerini açıklar."
            },
            {
                "question_en": "What critical energy infrastructure asset was the explicit target of this cyber attack?",
                "question_tr_hint": "Bu siber saldırının açık hedefi hangi kritik enerji altyapısı varlığıydı?",
                "correct_answer": "Trans-Anatolian natural gas pipeline turbine compression stations.",
                "distractors": [
                    "A residential rooftop solar hot-water tank heater in Izmir.",
                    "A commercial ice-cream storage warehouse refrigeration unit.",
                    "An electric streetcar trolley line in downtown Istanbul."
                ],
                "explanation_en": "Evelyn opens by stating the target is trans-Anatolian gas pipeline compression stations.",
                "explanation_tr": "Evelyn hedefin Trans-Anadolu doğal gaz boru hattı kompresör istasyonları olduğunu belirtir."
            }
        ],
        ["problem_solving", "cybersecurity"]
    ),
]
