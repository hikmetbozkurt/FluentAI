"""P8-004 Grammar micro-lessons C1-C2: nuance, discourse control, hedging, and Turkish L1 traps."""

GRAMMAR_C1_C2 = [
    # ==================== C1 (14 lessons) ====================
    {
        "id": "grammar.c1.dangling-participles-subject-alignment",
        "title": "Dangling Participles & Opening Modifier Subject Alignment",
        "cefr_level": "C1",
        "category": "relative_and_participle_clauses",
        "summary_en": "Ensure the implied doer of an opening participle phrase ('Reviewing the audit, ...') matches the exact grammatical subject of the main clause that follows.",
        "summary_tr": "Cümle başındaki ortaç yapısının ('Reviewing the log, ...') gizli öznesinin, virgülden sonra gelen ana cümlenin öznesiyle birebir örtüşmesini sağlayın.",
        "explanation_en": [
            {
                "title": "The Hidden Subject Rule in Fronted Modifiers",
                "content": "Concise executive memos frequently open with participle phrases such as 'Having tested offline playback, engineers cleared release.' If you follow with a passive or impersonal clause whose subject did not perform the opening verb ('*Having tested playback, the build was cleared'), the participle dangles ungrammatically.",
                "patterns": [
                    "V-ing / Having + V3, + [Agent Subject who performs the participle] + Verb",
                    "V3 (passive participle), + [Patient Subject acted upon] + Verb",
                ],
            }
        ],
        "explanation_tr": "Türkçede '-ince / -erek / -dikten sonra' ulaçlarının öznesi ana cümlenin öznesinden farklı olsa bile anlaşılır kalabilir; ancak İngilizcede virgülden sonraki ilk özne, baştaki eylemin faili sayılır.",
        "rules": [
            {
                "name": "Mandatory Subject Match After Comma",
                "pattern": "Participle Phrase (Agent X), + Subject X + Main Verb",
                "use_cases": ["Tightening incident summaries and executive memos without ambiguity"],
                "time_markers": ["having verified", "while auditing"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Checking the export twice, we noticed one missing header.",
                "structure_b": "Checking the export twice, one missing header surfaced.",
                "difference_explanation_en": "Sentence A pairs 'checking' with human subject 'we'; Sentence B implies a spreadsheet header checked the export.",
                "difference_explanation_tr": "A'da kontrol eden biziz ('we'); B'de ise başlığın kendisi dışa aktarımı kontrol etmiş gibi mantık hatası doğar.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "null_subject_transfer",
                "trap_title": "Edilgen Ana Cümleyle Asılı Ortaç (Dangling Participle) Kurmak",
                "explanation_tr": "'Raporu inceleyince önemli bir risk ortaya çıktı' yapısını '*Reviewing the report, an important risk appeared' şeklinde kurmak İngilizcede asılı ortaç hatası verir.",
                "incorrect_example": "Scanning the database for duplicates, several invalid keys were rejected.",
                "correct_example": "Scanning the database for duplicates, the validator rejected several invalid keys.",
            }
        ],
        "examples": [
            {
                "en": "Having benchmarked five compression profiles, the platform team selected lossless SQLite packing.",
                "tr": "Beş sıkıştırma profilini kıyasladıktan sonra platform ekibi kayıpsız SQLite paketlemesini seçti.",
                "rule_highlight": "Having benchmarked ... the platform team selected",
                "context": "architecture decision record",
            },
            {
                "en": "Built for zero-connectivity commutes, the mobile engine caches every drill locally.",
                "tr": "Bağlantısız yolculuklar için tasarlanan mobil motor her alıştırmayı yerel olarak önbellekler.",
                "rule_highlight": "Built for ... the mobile engine caches",
                "context": "product spec",
            },
        ],
        "topic_tags": ["participles", "writing", "syntax"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c1.concessive-as-though-fronting",
        "title": "Concessive Fronting with 'As' and 'Though' (Adj/Verb + As + Subject)",
        "cefr_level": "C1",
        "category": "inversion_and_emphasis",
        "summary_en": "Front an adjective, adverb, or bare verb directly before 'as' or 'though' ('Compelling as that pitch sounds...') to concede a point gracefully before pivoting.",
        "summary_tr": "Bir karşı görüşü zarifçe teslim edip kendi değerlendirmenize geçerken 'Sıfat/Zarf/Fiil + as/though + Özne + Fiil' devrik yapısını kullanın.",
        "explanation_en": [
            {
                "title": "Refined Concession Without Repeating 'Although Very...'",
                "content": "Instead of writing 'Although the timeline seems aggressive, ...', C1 rhetoric often moves the key adjective forward: 'Aggressive as the timeline seems, phased validation makes it realistic.' Fronted singular countable nouns drop 'a/an' ('Executive though she is, ...').",
                "patterns": [
                    "Adjective / Adverb + as / though + Subject + Verb, + Main Clause",
                    "Bare Infinitive + as / though + Subject + may / might, + Main Clause",
                ],
            }
        ],
        "explanation_tr": "Türkçedeki 'Her ne kadar ikna edici görünse de...' veya 'Ne kadar çabalarsak çabalayalım...' vurgusu İngilizcede 'Try though we may...' ya da 'Persuasive as it sounds...' kalıbıyla çok doğal verilir.",
        "rules": [
            {
                "name": "Concessive Predicate Fronting",
                "pattern": "Adj / Adv / Noun (no article) + as/though + Subject + Verb",
                "use_cases": ["Acknowledging opponent strengths diplomatically in steering reviews"],
                "time_markers": ["nevertheless", "yet"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Although the license appears expensive, it caps long-term exposure.",
                "structure_b": "Expensive as the license appears, it caps long-term exposure.",
                "difference_explanation_en": "Both state concession; fronting 'Expensive' creates sharper contrast and executive rhythm.",
                "difference_explanation_tr": "'Expensive' sıfatını öne çekmek sıradan bir 'although' cümlesine kıyasla üst düzey vurgu kazandırır.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "Sıfat Öne Çekildiğinde 'As' Yerine 'Although' Dizmek veya Artikeli Bırakmak",
                "explanation_tr": "'*Compelling although it is' ya da '*A challenger as he is' şeklindeki dizimler hatalıdır; önde isim varsa artikel atılır ve bağlaç 'as/though' olur.",
                "incorrect_example": "Attractive although this discount looks, unit margins remain tight.",
                "correct_example": "Attractive as this discount looks, unit margins remain tight.",
            }
        ],
        "examples": [
            {
                "en": "Tempting though it may be to bypass integration QA, schema drift quickly surfaces.",
                "tr": "Entegrasyon QA'yı atlamak her ne kadar cazip görünse de şema sapması hızla ortaya çıkar.",
                "rule_highlight": "Tempting though it may be",
                "context": "engineering governance",
            },
            {
                "en": "Much as I appreciate your candor, our Q3 security freeze leaves no wiggle room.",
                "tr": "Açık sözlülüğünüzü ne kadar takdir etsem de 3. çeyrek güvenlik dondurmamız esneklik alanı bırakmıyor.",
                "rule_highlight": "Much as I appreciate",
                "context": "negotiation reply",
            },
        ],
        "topic_tags": ["diplomacy", "register", "emphasis"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c1.diplomatic-past-distancing",
        "title": "Diplomatic & Attitudinal Past for Tentative Requests and Soft Objections",
        "cefr_level": "C1",
        "category": "tenses_and_aspect",
        "summary_en": "Use Past Simple or Past Continuous ('Did you want...? / I was hoping... / I wondered whether...') in present conversations to create respectful social distance rather than past time reference.",
        "summary_tr": "Şu an konuşulan nazik isteklerde veya itirazlarda geçmiş zamanı ('I was wondering / Did you need...') zaman olarak değil sosyal mesafe ve nezaket yumuşatıcısı olarak kullanın.",
        "explanation_en": [
            {
                "title": "Social Politeness Through Tense Backshifting",
                "content": "When speaking to senior clients or cross-functional directors right now, 'I wondered if we might adjust pricing timing' softens urgency far more tactfully than bare Present Simple imperatives. Progressive past ('was hoping / was thinking') sounds even more open to decline politely.",
                "patterns": [
                    "I was hoping / wondering + if/whether + Subject + could/might...",
                    "Did you want to review + Object + before tomorrow?",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Merak ediyordum acaba...' biçiminde geçmiş hikayeleme zaten vardır; ancak 'Şu an bunu mu istiyordunuz?' sorusunun şimdiki zamanla ('I want now') sorulması bazen fazla keskin duyulabilir.",
        "rules": [
            {
                "name": "Non-Temporal Politeness Past",
                "pattern": "I wondered / was hoping / thought we might + V1 (present dialogue)",
                "use_cases": ["Soliciting concessions, requesting schedule extensions, and proposing edits politely"],
                "time_markers": ["right now", "when you have a moment"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "I want us to move the milestone by four days.",
                "structure_b": "I was hoping we might shift the milestone by four days.",
                "difference_explanation_en": "Sentence A states a flat demand; Sentence B leaves respectful room for client counter-proposals.",
                "difference_explanation_tr": "A doğrudan talep koyarken B karşı tarafa saygılı müzakere alanı bırakır.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "tense_transfer",
                "trap_title": "Diplomatik Geçmiş Zamanı Gerçek Geçmiş Sanıp Yanlış Yanıtlamak",
                "explanation_tr": "'I was wondering whether you had capacity this Friday' cümlesi geçmişi sormaz; bu cuma için nazikçe müsaitlik yoklar.",
                "incorrect_example": "Give me your final figures by three, I want to close today.",
                "correct_example": "I was hoping we might finalize your figures by three so payroll clears cleanly.",
            }
        ],
        "examples": [
            {
                "en": "I wondered whether legal might review clause six before tomorrow's steering session.",
                "tr": "Yarınki yürütme oturumundan önce hukuk ekibinin altıncı maddeyi inceleyip inceleyemeyeceğini merak ediyordum.",
                "rule_highlight": "wondered whether legal might",
                "context": "executive outreach",
            },
            {
                "en": "Did you want us to include regional sub-totals in the board slide deck as well?",
                "tr": "Yönetim kurulu sunumuna bölgesel alt toplamları da eklememizi ister miydiniz?",
                "rule_highlight": "Did you want us to include",
                "context": "client meeting",
            },
        ],
        "topic_tags": ["diplomacy", "register", "tenses"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c1.counterfactual-but-for-otherwise",
        "title": "Implicit Counterfactuals: 'But For', 'Without', and 'Otherwise / Or Else'",
        "cefr_level": "C1",
        "category": "conditionals_and_hypotheticals",
        "summary_en": "Express Type 2 and Type 3 conditional realities succinctly without an 'if'-clause by deploying 'But for + Noun', 'Without + Gerund', and mid-argument 'Otherwise'.",
        "summary_tr": "Uzun bir 'if' yan cümlesi kurmadan 'But for + İsim' (...olmasaydı), 'Without + -ing' ve 'Otherwise' (aksi takdirde) yapılarıyla özlü karşı-olgusal sonuçlar üretin.",
        "explanation_en": [
            {
                "title": "Compact Conditional Compression in Senior Prose",
                "content": "'If it had not been for automated determinism checks, bad hashes would have shipped' compresses cleanly to 'But for automated determinism checks, bad hashes would have shipped.' Notice 'otherwise' replaces a full negative 'if'-clause cleanly between clauses.",
                "patterns": [
                    "But for + Noun / Noun Phrase, + Subject + would (have) + Verb",
                    "Real Clause; otherwise, + Subject + would (have) + Verb",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Şu kontrol olmasaydı çoktan hata çıkmıştı' yapısı sıkça uzun 'if it hadn't been...' ile çevrilir. C1 seviyesinde 'But for + İsim' kalıbı hem çok kısa hem de resmi toplantılarda etkileyicidir.",
        "rules": [
            {
                "name": "Prepositional Counterfactual ('But For')",
                "pattern": "But for + Noun/NP, + Subject + would have V3 (past) / would V1 (present)",
                "use_cases": ["Highlighting critical safeguards in risk reviews and post-mortems"],
                "time_markers": ["yesterday", "without safeguard X"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "The validator caught it; otherwise, broken IDs would have packaged.",
                "structure_b": "The validator caught it; otherwise, broken IDs will package.",
                "difference_explanation_en": "Counterfactual past reality triggers 'would have V3' after 'otherwise', not future 'will'.",
                "difference_explanation_tr": "Gerçekleşmiş geçmiş korumaya atıfta 'otherwise' sonrası 'will' değil 'would have V3' gelir.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "conditional_overgeneralization",
                "trap_title": "'But For' Kalıbını İstisna Anlamındaki 'Except' İle Karıştırmak",
                "explanation_tr": "'But for X' burada '...hariç' değil 'X olmasaydı' anlamına gelen tam bir koşul yapısıdır ve ana cümlede mutlaka 'would/could/might (have)' ister.",
                "incorrect_example": "But for your quick patch, client invoices failed yesterday.",
                "correct_example": "But for your quick patch, client invoices would have failed yesterday.",
            }
        ],
        "examples": [
            {
                "en": "But for local SQLite prepackaging, passengers in tunnel commutes would lose every session.",
                "tr": "Yerel SQLite ön paketlemesi olmasaydı tünel yolculuklarındaki kullanıcılar tüm oturumları kaybederdi.",
                "rule_highlight": "But for local SQLite prepackaging ... would lose",
                "context": "architecture justification",
            },
            {
                "en": "Counsel spotted the renewal trap early; otherwise, we would have locked into five-year rates.",
                "tr": "Hukuk ekibi yenileme tuzağını erken yakaladı; aksi takdirde beş yıllık oranlara kilitlenmiştik.",
                "rule_highlight": "otherwise, we would have locked",
                "context": "vendor negotiation",
            },
        ],
        "topic_tags": ["conditionals", "risk", "writing"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c1.reporting-attitude-modal-backshift",
        "title": "Modal Nuances in Indirect Reports: Shifting 'Must', 'Can', and 'May'",
        "cefr_level": "C1",
        "category": "reported_speech",
        "summary_en": "Separate internal obligation ('must' -> 'had to' or kept 'must' for ongoing rules) from logical deduction ('must have V3' stays unchanged), while backshifting 'can/may/will'.",
        "summary_tr": "Dolaylı aktarımda zorunluluk bildiren 'must'ı 'had to'ya çekerken çıkarım bildiren 'must'ı koruyun; 'can/may/will' kiplerini aktarım zamanına göre uyumlu dönüştürün.",
        "explanation_en": [
            {
                "title": "Why Deduction Modals Resist Backshifting",
                "content": "When reporting past necessity ('You must sign today'), shift to 'She reminded us we had to sign that day.' However, when reporting logical certainty ('She said it must be a cache bug' or 'must have stalled earlier'), 'must' stays unchanged because English lacks '*had to have' for deduction.",
                "patterns": [
                    "Reported Duty: 'You must audit' -> He insisted we had to audit.",
                    "Reported Deduction: 'It must be delayed' -> He noted it must be delayed.",
                ],
            }
        ],
        "explanation_tr": "Türkçe aktarımda kip ayrımı bağlamdan çıkar; İngilizcede ise 'zorunda kaldığını söyledi' ('had to') ile 'olmuş olmalı dedi' ('must have') yapısal olarak ayrışır.",
        "rules": [
            {
                "name": "Obligation vs. Epistemic Modal Reporting",
                "pattern": "Reporting Past Verb + that + Clause (had to = duty | might/must have = inference)",
                "use_cases": ["Summarizing legal counsel guidance and engineering post-mortem calls accurately"],
                "time_markers": ["during Tuesday's briefing", "under prior rules"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "She noted we had to encrypt local backups before shipping.",
                "structure_b": "She noted the timeout must have occurred before failover.",
                "difference_explanation_en": "Sentence A shifts binding obligation ('must' -> 'had to'); Sentence B keeps epistemic certainty intact.",
                "difference_explanation_tr": "A cümlesi zorunluluğu ('had to'), B cümlesi ise geçmiş çıkarımı ('must have') korur.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "tense_transfer",
                "trap_title": "Geçmiş Çıkarım Bildiren 'Must' Yerine '*Had To Have' Kurmak",
                "explanation_tr": "Ana fiil geçmiş diye tahmin bildiren 'must'ı 'had to'ya çevirmek anlamı zorunluluğa çevirir ve bozar.",
                "incorrect_example": "The on-call engineer guessed that the DNS node had to fail at midnight.",
                "correct_example": "The on-call engineer guessed that the DNS node must have failed at midnight.",
            }
        ],
        "examples": [
            {
                "en": "Security emphasized that external agencies would need temporary scoped tokens rather than master keys.",
                "tr": "Güvenlik ekibi dış ajansların ana anahtarlar yerine sınırlı geçici belirteçlere ihtiyaç duyacağını vurguladı.",
                "rule_highlight": "emphasized that ... would need",
                "context": "security review",
            },
            {
                "en": "Site coordinators said we might switch to the quieter boardroom whenever ready.",
                "tr": "Saha koordinatörleri hazır olduğumuzda daha sakin toplantı odasına geçebileceğimizi söyledi.",
                "rule_highlight": "said we might switch",
                "context": "executive offsite",
            },
        ],
        "topic_tags": ["reported_speech", "modals", "legal"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c1.infinitive-passive-perfect-nuance",
        "title": "Passive & Perfect Infinitives: 'To Have Done', 'To Be Audited'",
        "cefr_level": "C1",
        "category": "verb_patterns_and_infinitives",
        "summary_en": "Indicate earlier time inside infinitive clauses using perfect aspect ('seems to have drifted') and passiveness using 'to be + V3' or 'to have been + V3'.",
        "summary_tr": "Mastar yapılarının içinde ana fiilden daha önceki bir zamanı anlatmak için 'to have V3', edilgenlik için 'to be V3 / to have been V3' kullanın.",
        "explanation_en": [
            {
                "title": "Encoding Timeline & Voice Inside Non-Finite Verbs",
                "content": "Compare 'Telemetry seems to show a dip' (present match) against 'Telemetry seems to have spiked overnight' (earlier completed event seen now) and 'The log appears to have been truncated' (earlier + passive).",
                "patterns": [
                    "Present/Simultaneous Passive: appears / is expected + to be + V3",
                    "Earlier Active / Passive: seems / is thought + to have + (been) + V3",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Dün gece kopmuş görünüyor' kalıbı kolay kurulur; İngilizcede bunu sadece '*seems to drop last night' yazmak zaman uyumsuzluğu yaratır, 'seems to have dropped' gerekir.",
        "rules": [
            {
                "name": "Anterior Time via Perfect Infinitive",
                "pattern": "Verb/Adj + to have + V3 (action prior to main verb)",
                "use_cases": ["Hedging diagnostic findings and attributing prior causes carefully"],
                "time_markers": ["overnight", "earlier", "prior to merge"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "The sync script seems to slow down under heavy batches.",
                "structure_b": "The sync script seems to have stalled during midnight maintenance.",
                "difference_explanation_en": "Simple infinitive fits ongoing behavior; perfect infinitive refers back to a finished prior episode.",
                "difference_explanation_tr": "Yalın mastar genel davranışı, 'to have stalled' ise geçmişte yaşanıp biten olayı işaret eder.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "aspect_confusion",
                "trap_title": "Geçmiş Zaman Zarfıyla Yalın Mastar ('To Happen Yesterday') Kullanmak",
                "explanation_tr": "Ana fiil geniş zamanda kalıp ('is likely / appears') olay geçmişteyse yalın mastar yerine 'to have (been) V3' kurulmalıdır.",
                "incorrect_example": "The backup token appears to expire three days ago.",
                "correct_example": "The backup token appears to have expired three days ago.",
            }
        ],
        "examples": [
            {
                "en": "Every curriculum package is scheduled to be audited deterministically before signing.",
                "tr": "Her müfredat paketinin imzalanmadan önce deterministik olarak denetlenmesi planlanıyor.",
                "rule_highlight": "is scheduled to be audited",
                "context": "release pipeline",
            },
            {
                "en": "We are fortunate to have caught those fifteen vocabulary collisions prior to packaging.",
                "tr": "Paketlemeden önce o on beş kelime çakışmasını yakalamış olduğumuz için şanslıyız.",
                "rule_highlight": "to have caught",
                "context": "QA review",
            },
        ],
        "topic_tags": ["infinitives", "tenses", "engineering"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c1.get-passives-and-adversative-haves",
        "title": "Adversative 'Have' & Event vs. State Passives ('Was Broken' vs. 'Got Flagged')",
        "cefr_level": "C1",
        "category": "passive_and_causative",
        "summary_en": "Distinguish event passives ('got delayed', 'had accounts suspended') from resulting state descriptions ('remained locked'), and understand involuntary 'have Object V3' setbacks.",
        "summary_tr": "Eylem odaklı edilgenlik ('got flagged') ile sonuç durumunu ('stayed locked') ayırın ve kişinin başına gelen istenmeyen olayları 'had + Nesne + V3' ile ifade edin.",
        "explanation_en": [
            {
                "title": "Delegated Service vs. Unwanted Experience",
                "content": "The structure 'We had our account verified' describes commissioned action. Identical syntax—'We had two flights canceled'—expresses an unwelcome incident affecting 'us'. Similarly, 'get + V3' emphasizes dynamic event occurrence ('three items got rejected').",
                "patterns": [
                    "Adversative Experiential: Subject + had + Object + V3 (unwelcome setback)",
                    "Dynamic Event Passive: Subject + got + V3 (outcome event emphasis)",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Uçuşumuzu iptal ettirdik' ile 'Uçuşumuz iptal edildi (başımıza geldi)' ayrılır. İngilizcede 'We had our flight canceled' bağlama göre beklenmedik mağduriyeti anlatır ve bizzat iptal ettirdik demek değildir.",
        "rules": [
            {
                "name": "Context Signals Between Causative vs. Experiential 'Have'",
                "pattern": "Experienced Subject + had + Affected Asset + Unwelcome V3",
                "use_cases": ["Narrating disruptions, customs holds, and denied approvals naturally"],
                "time_markers": ["last week", "at customs"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "The vendor had their security badge renewed.",
                "structure_b": "The vendor had their staging access revoked after failing QA.",
                "difference_explanation_en": "Positive 'renewed' signals arranged service; negative 'revoked' signals an adverse penalty imposed on the vendor.",
                "difference_explanation_tr": "Olumlu fiil ('renewed') yaptırılan işlemi; olumsuz fiil ('revoked') ise maruz kalınan yaptırımı gösterir.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "voice_and_causative_transfer",
                "trap_title": "Deneyim Bildiren 'Had My Laptop Stolen' Kalıbını 'Çaldırdım (Yaptırdım)' Sanmak",
                "explanation_tr": "'We had our server blocked' cümlesi 'Sunucumuzu bilerek engellettik' demek değildir; dış bir etken tarafından sunucumuza bu durumun yaşatıldığını anlatır.",
                "incorrect_example": "Our courier was lost three pallets at regional customs yesterday.",
                "correct_example": "Our courier had three pallets held at regional customs yesterday.",
            }
        ],
        "examples": [
            {
                "en": "Nearly every misnamed file got caught automatically during overnight validation.",
                "tr": "Neredeyse tüm hatalı adlandırılmış dosyalar gece doğrulamasında otomatik olarak yakalandı.",
                "rule_highlight": "got caught automatically",
                "context": "CI engineering",
            },
            {
                "en": "Three traveling consultants had their roaming passes throttled mid-conference.",
                "tr": "Seyahat eden üç danışmanın dolaşım paketleri konferans ortasında kısıtlandı.",
                "rule_highlight": "had their roaming passes throttled",
                "context": "travel incident",
            },
        ],
        "topic_tags": ["passive", "causative", "operations"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c1.comparative-clause-ellipsis-inversion",
        "title": "Formal Comparative Inversion & Auxiliary Ellipsis ('Than Did Legacy Builds')",
        "cefr_level": "C1",
        "category": "discourse_markers_and_cohesion",
        "summary_en": "Avoid repeating whole predicates after 'than' and 'as' using accurate auxiliary pro-forms, and optionally invert after 'than/as' when comparing heavy nominal subjects in formal writing.",
        "summary_tr": "'Than' ve 'as' sonrasında fiili tekrarlamak yerine uygun yardımcı fiille eksiltme ('than we expected') yapın; ağır öznelerde resmi devrik dizim ('than did earlier systems') kurun.",
        "explanation_en": [
            {
                "title": "Cleaner Prose Without Trailing Auxiliary Mismatches",
                "content": "When comparing actions cleanly, match the auxiliary tense ('performs more smoothly than legacy builds did' or 'than did legacy builds' in formal reports). Always match auxiliary families: be -> be, have -> have, action -> do/did.",
                "patterns": [
                    "Comparative + than / as + Auxiliary + Subject (formal balanced weight)",
                    "Comparative + than / as + Subject + Auxiliary (neutral)",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'önceki sürümlerden daha hızlı' tek öbekte kapanır. İngilizce karşılaştırma tümcesinde yardımcı fiil zaman uyumu unutulursa ('*works faster than it was before') hata doğar; doğrusu 'than it did' olmalıdır.",
        "rules": [
            {
                "name": "Matching Comparison Auxiliaries",
                "pattern": "V (action) ... than + Subject + do/does/did | be/modal ... than + Subject + be/modal",
                "use_cases": ["Comparing quarterly revenue, latency metrics, or learner outcomes formally"],
                "time_markers": ["this year", "than in Q1"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "The app loads faster than it did in version 4.",
                "structure_b": "The new indexer preserves context better than did earlier regex scripts.",
                "difference_explanation_en": "Both match do/did cleanly; B inverts 'did earlier regex scripts' to balance a long noun phrase smoothly.",
                "difference_explanation_tr": "İkisi de doğru yardımcı fiili eşler; B uzun özne nedeniyle 'did'i öne alarak denge sağlar.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "Karşılaştırmada Eylem Fiiline Karşılık Yanlışlıkla 'Was/Is' Kullanmak",
                "explanation_tr": "Ana fiil 'worked / shipped / grew' ise kıyaslamanın sonunda '*than it is/was before' değil 'than it did' kullanılmalıdır.",
                "incorrect_example": "Subscriptions grew twice as smoothly in Q4 as they were in Q2.",
                "correct_example": "Subscriptions grew twice as smoothly in Q4 as they did in Q2.",
            }
        ],
        "examples": [
            {
                "en": "Deterministic tests safeguard data integrity more reliably than manual checks ever could.",
                "tr": "Deterministik testler veri bütünlüğünü manuel kontrollerin olabileceğinden çok daha güvenilir korur.",
                "rule_highlight": "than manual checks ever could",
                "context": "testing strategy",
            },
            {
                "en": "Regional partners adopted self-serve billing earlier than did headquarters affiliates.",
                "tr": "Bölge ortakları self-servis faturalamayı merkez iştiraklerinden daha erken benimsedi.",
                "rule_highlight": "than did headquarters affiliates",
                "context": "executive summary",
            },
        ],
        "topic_tags": ["comparisons", "ellipsis", "writing"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c1.genitive-with-gerunds-formal",
        "title": "Possessives Before Gerund Clauses in Formal Registers ('Their Delaying Launch')",
        "cefr_level": "C1",
        "category": "verb_patterns_and_infinitives",
        "summary_en": "Use possessive determiners ('my / our / their / the vendor's') before a gerund when the entire action forms the formal subject or prepositional object.",
        "summary_tr": "Resmi yazışmalarda bir eylemin kendisi özne ya da edat nesnesi olduğunda ulaç ('-ing') önüne nesne zamiri ('him / them') yerine iyelik belirteci ('his / their / company's') koyun.",
        "explanation_en": [
            {
                "title": "Focusing on the Event Rather Than the Person",
                "content": "In speech, 'We appreciate them sharing logs early' passes comfortably. In legal and formal executive memos, possessive modification ('We appreciate their sharing logs early' or 'the board's approving the audit') keeps focus on the act itself and avoids ambiguity.",
                "patterns": [
                    "Possessive Determiner (our / their / the client's) + V-ing + Object",
                    "Object Pronoun (them / us) + V-ing (informal conversational alternative)",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Onların gecikmesi sorunu büyüttü' cümlesinde '-ın' iyelik eki doğal olduğu halde öğrenciler bunu İngilizceye '*Them delaying' diye aktarır. Resmi metinde 'Their delaying' tercih edilir.",
        "rules": [
            {
                "name": "Formal Gerund Subject Possessive",
                "pattern": "His / Their / Noun's + Gerund (-ing) + Singular Verb",
                "use_cases": ["Drafting formal escalations, audit letters, and polite executive acknowledgments"],
                "time_markers": ["despite", "prior to", "upon"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Legal's flagging that indemnity trap saved us thousands.",
                "structure_b": "We caught the clerk entering manual overrides.",
                "difference_explanation_en": "With '-ing' as a gerund event ('Legal's flagging'), use possessive; with perception verbs ('caught him entering'), use object pronoun.",
                "difference_explanation_tr": "Olayın kendisi kastedildiğinde iyelik ('Legal's flagging'); duyu fiillerinde yakalama eyleminde ('caught the clerk') nesne hali seçilir.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "null_subject_transfer",
                "trap_title": "Cümle Başında Çıplak Nesne Zamiriyle Gerund Öznesi Başlatmak",
                "explanation_tr": "'Bizim geç kalmamız' anlamında cümleyi '*Us arriving late caused concern' şeklinde açmak resmi yazıda standart dışıdır; doğrusu 'Our arriving late' veya 'Our late arrival'dır.",
                "incorrect_example": "Him ignoring three schema warnings stalled yesterday's build.",
                "correct_example": "His ignoring three schema warnings stalled yesterday's build.",
            }
        ],
        "examples": [
            {
                "en": "We genuinely appreciate your sharing the regional tax changes well ahead of schedule.",
                "tr": "Bölgesel vergi değişikliklerini takvimden oldukça önce paylaşmanızı gerçekten takdir ediyoruz.",
                "rule_highlight": "your sharing the regional tax changes",
                "context": "executive email",
            },
            {
                "en": "The regulator's granting a thirty-day grace window eased regional onboarding strain.",
                "tr": "Düzenleyicinin otuz günlük geçiş süresi tanıması bölgesel uyum yükünü hafifletti.",
                "rule_highlight": "The regulator's granting",
                "context": "policy memo",
            },
        ],
        "topic_tags": ["gerunds", "writing", "register"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c1.discourse-reformulation-clarifiers",
        "title": "Precision Reformulation & Self-Correction Markers: 'Or Rather', 'That Is to Say'",
        "cefr_level": "C1",
        "category": "discourse_markers_and_cohesion",
        "summary_en": "Use explicit reformulation bridges ('or rather', 'that is to say', 'more specifically', 'put another way') in speech and writing to sharpen vague terms without sounding indecisive.",
        "summary_tr": "Konuşma veya yazıda ilk ifadeyi daha isabetli düzeltmek veya somutlaştırmak için 'or rather' (daha doğrusu), 'that is to say' (yani) ve 'put another way' bağlaçlarını doğru noktalayın.",
        "explanation_en": [
            {
                "title": "Refining Claims Gracefully Mid-Thought",
                "content": "Strong C1 speakers rarely restart an entire sentence awkwardly; instead they insert targeted clarifiers: 'or rather' repairs word accuracy, 'that is to say' translates jargon into plain implications, and 'put another way' summarizes action consequences.",
                "patterns": [
                    "Broad Statement—or rather, Sharper Correction",
                    "Technical Claim—that is to say, + Practical Implication",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'yani', 'daha doğrusu' sıkça her yere 'I mean' ile çevrilir. İş İngilizcesinde resmi toplantılar ve sunumlarda 'or rather', 'more precisely' veya 'in plain terms' çok daha otoriter duyulur.",
        "rules": [
            {
                "name": "Selecting the Exact Reformulation Tool",
                "pattern": "Correction = or rather | Paraphrase = put differently | Specification = more precisely",
                "use_cases": ["Clarifying complex technical findings during Q&A sessions"],
                "time_markers": ["to clarify", "specifically"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Latency stayed steady—or rather, p95 latency dipped slightly.",
                "structure_b": "Deterministic builds stay fixed—that is, identical YAML produces identical SHA hashes.",
                "difference_explanation_en": "'Or rather' narrows or corrects a looser term; 'that is' unpacks what the term practically means.",
                "difference_explanation_tr": "'Or rather' önceki ifadeyi daha isabetli düzeltir; 'that is' ise kavramın ne anlama geldiğini açar.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "conditional_overgeneralization",
                "trap_title": "Resmi Sunumda Her Düzeltmede Yalnızca 'I Mean' Kullanmak",
                "explanation_tr": "Sürekli 'I mean...' tekrarı konuşmacıyı tereddütlü gösterir; 'or rather' ve 'more precisely' ile kendinden emin düzeltme yapın.",
                "incorrect_example": "The database is frozen, I mean, the schema version won't migrate automatically.",
                "correct_example": "The schema is locked—more precisely, any version jump requires an explicit migration.",
            }
        ],
        "examples": [
            {
                "en": "The launch relies on local assets—that is to say, offline learners never wait on servers.",
                "tr": "Lansman yerel varlıklara dayanıyor; yani çevrimdışı öğrenciler asla sunucuyu beklemiyor.",
                "rule_highlight": "that is to say",
                "context": "product briefing",
            },
            {
                "en": "Regional sales slowed—or rather, enterprise deals pushed signature dates into January.",
                "tr": "Bölge satışları yavaşladı; daha doğrusu kurumsal anlaşmalar imza tarihlerini Ocak ayına öteledi.",
                "rule_highlight": "or rather",
                "context": "finance readout",
            },
        ],
        "topic_tags": ["reformulation", "speaking", "discourse_markers"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c1.quantifier-agreement-distributive-nouns",
        "title": "Complex Subject-Verb Agreement: 'The Number of' vs. 'A Number of' & Correlatives",
        "cefr_level": "C1",
        "category": "noun_phrases_and_articles",
        "summary_en": "Apply semantic agreement: 'the number of + Plural' takes a singular verb (counting one figure), 'a number of + Plural' takes plural ('several'), and 'neither A nor B' matches the closer subject.",
        "summary_tr": "'The number of' tek bir sayısal değeri anlattığı için tekil fiil alırken, 'a number of' ('birçok') çoğul fiil alır; 'neither... nor' da ise fiil yakın özneye uyar.",
        "explanation_en": [
            {
                "title": "Head Noun vs. Proximate Noun Agreement",
                "content": "Long academic or audit subjects often trap writers by putting plural nouns right next to singular heads ('Each of the five regional branches HAS verified', 'The number of duplicate entries HAS dropped to zero'). Meanwhile, 'neither/either... or/nor' agrees with whatever stands nearest the verb.",
                "patterns": [
                    "the number of + Plural Noun + Singular Verb ('the number has fallen')",
                    "a number of + Plural Noun + Plural Verb ('a number of items have passed')",
                    "neither X nor [Closer Noun] + Verb matching Closer Noun",
                ],
            }
        ],
        "explanation_tr": "Türkçede özne çoğul veya bileşik olsa da fiil çoğu kez tekil çekilir ('birçok dosya eklendi'). İngilizcede ise 'a number of files HAVE passed' ile 'each of the files HAS passed' ayrımı keskindir.",
        "rules": [
            {
                "name": "Proximity & Head-Noun Agreement",
                "pattern": "Each/Every one of + Plural + Singular Verb | A majority/percent of + [Noun] + Verb matching Noun",
                "use_cases": ["Reporting data audit metrics and compliance percentages cleanly"],
                "time_markers": ["this quarter", "across batches"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "The number of unresolved bugs is dropping daily.",
                "structure_b": "A number of minor UI bugs are still awaiting review.",
                "difference_explanation_en": "'The number' = one scalar count ('is'); 'A number of' = several items ('are').",
                "difference_explanation_tr": "'The number' tekil sayı istatistiğidir ('is'); 'A number of' ise 'birkaç/çok sayıda' anlamında çoğuldur ('are').",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "null_subject_transfer",
                "trap_title": "'Each of / One of + Çoğul İsim' Sonrası Çoğul Fiil Çekmek",
                "explanation_tr": "'Belgelerin her biri onaylandı' yapısında sondaki çoğul isme aldanıp '*Each of the documents were signed' demek yanlıştır; baş özne 'Each' tekildir ('was signed').",
                "incorrect_example": "Neither the audit lead nor her regional deputies has objections.",
                "correct_example": "Neither the audit lead nor her regional deputies have objections.",
            }
        ],
        "examples": [
            {
                "en": "Every single one of the new vocabulary checks has passed automated validation.",
                "tr": "Yeni kelime kontrollerinin istisnasız her biri otomatik doğrulamadan geçti.",
                "rule_highlight": "Every single one ... has passed",
                "context": "validation report",
            },
            {
                "en": "Sixty percent of enterprise training budget now goes toward interactive speaking.",
                "tr": "Kurumsal eğitim bütçesinin yüzde altmışı artık etkileşimli konuşma pratiğine ayrılıyor.",
                "rule_highlight": "Sixty percent of ... budget ... goes",
                "context": "market report",
            },
        ],
        "topic_tags": ["agreement", "data", "writing"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c1.negative-infinitive-split-position",
        "title": "Scope of Negation: Placing 'Not' in Infinitives, Clauses, and Modals",
        "cefr_level": "C1",
        "category": "modals_and_semi_modals",
        "summary_en": "Change clause meaning cleanly by shifting negation placement ('decided not to launch' vs. 'didn't decide to launch') and separating 'must not' (prohibition) from 'need not / don't have to' (no obligation).",
        "summary_tr": "'Karar vermedi' ile 'yapmama kararı aldı' arasındaki farkı 'not to V1' konumunu doğru seçerek kurun; yasak bildiren 'must not'ı gereksizlik bildiren 'need not'tan ayırın.",
        "explanation_en": [
            {
                "title": "Where 'Not' Lands Changes Legal and Operational Intent",
                "content": "In contract and planning notes, 'We advised him not to sign' commands restraint, whereas 'We didn't advise him to sign' simply reports silence. Also put 'not' directly before 'to': 'not to rush', avoiding awkward split-negation unless emphasizing contrast.",
                "patterns": [
                    "Verb + not + to + Infinitive (explicit decision to refrain)",
                    "need not + V1 / doesn't have to + V1 (optionality) vs. must not + V1 (strict ban)",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'yapmamamızı söyledi' ile 'yapmamızı söylemedi' ince fark taşır. Ayrıca 'needn't / don't have to' mecburiyetsizliği anlatırken 'mustn't' kesin yasak bildirir.",
        "rules": [
            {
                "name": "Negative Non-Finite Positioning",
                "pattern": "Main Verb + (Object) + not + to-Infinitive / Participle",
                "use_cases": ["Drafting clear SOP instructions, compliance prohibitions, and executive decisions"],
                "time_markers": ["under policy", "before merge"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Reviewers needn't audit unchanged seed files twice.",
                "structure_b": "Reviewers mustn't commit raw API tokens into test fixtures.",
                "difference_explanation_en": "'Needn't' saves unnecessary work (optional); 'mustn't' states an absolute security ban.",
                "difference_explanation_tr": "'Needn't' gerek olmadığını (isteğe bağlılığı), 'mustn't' ise kesin yasağı belirtir.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "tense_transfer",
                "trap_title": "Yasak Yerine Gereksizlik İçin 'Mustn't' Kullanmak",
                "explanation_tr": "'Toplantıya katılmak zorunda değilsin' anlamında '*You mustn't join' demek 'Katılman yasak' gibi sert ve yanlış bir mesaj verir; doğrusu 'You don't have to / needn't join'dir.",
                "incorrect_example": "Contractors decided to not upload debug traces, though they mustn't use staging.",
                "correct_example": "Contractors decided not to upload debug traces, though they needn't wipe staging.",
            }
        ],
        "examples": [
            {
                "en": "The committee opted not to expand third-party tracking before Q2.",
                "tr": "Komite ikinci çeyrekten önce üçüncü taraf izlemeyi genişletmeme kararı aldı.",
                "rule_highlight": "opted not to expand",
                "context": "policy minutes",
            },
            {
                "en": "Offline flashcards must not rely on live API lookups when network bars disappear.",
                "tr": "Çevrimdışı kartlar ağ sinyali kesildiğinde canlı API sorgularına dayanmamalıdır.",
                "rule_highlight": "must not rely",
                "context": "architecture rule",
            },
        ],
        "topic_tags": ["modals", "legal", "negation"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c1.restrictive-which-what-whatever",
        "title": "Universal Concession & Free Relatives: 'Whatever', 'However', 'Whichever'",
        "cefr_level": "C1",
        "category": "relative_and_participle_clauses",
        "summary_en": "Handle universal conditions and open choices cleanly using 'however + adj/adv', 'whichever' (bounded set), and 'whatever' (open range) cleanly.",
        "summary_tr": "Sınırsız veya koşulsuz kuralları ifade ederken 'however + sıfat/zarf' (ne kadar ... olursa olsun), 'whichever' (sınırlı seçenekten hangisi) ve 'whatever' yapılarını doğru sırada kurun.",
        "explanation_en": [
            {
                "title": "'However + Adjective' Always Pulls Its Adjective Forward",
                "content": "Unlike conversational 'how', concessive 'however' behaves like 'no matter how': pull the modified adjective or adverb right after 'however' before writing Subject + Verb ('However urgent the ticket is...', NOT '*However the ticket is urgent').",
                "patterns": [
                    "However + Adj / Adv + Subject + Verb (concessive degree)",
                    "Whichever + [Bounded Option] + Subject + chooses, + Main Clause",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Talep ne kadar acil olursa olsun' cümlesinde sıfat yükleme yakın durur; İngilizcede ise 'however' sıfatı hemen yanına çeker ('However small the error is'). Ayrıca sınırlı seçenekte 'whatever' yerine 'whichever' seçilir.",
        "rules": [
            {
                "name": "Degree-Pull with 'However'",
                "pattern": "However + Adjective/Adverb + Subject + Verb (+ may be)",
                "use_cases": ["Setting unconditional SLAs, safety mandates, and fallback contracts"],
                "time_markers": ["regardless of", "at any tier"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Whichever of the two pricing tiers you choose, offline review stays included.",
                "structure_b": "Whatever feedback comes out of testing, publish the raw scorecard.",
                "difference_explanation_en": "'Whichever' selects from the defined pair (two tiers); 'whatever' covers any unspecified future feedback.",
                "difference_explanation_tr": "'Whichever' belli iki seçenek arasından seçim yaparken 'whatever' sınırsız genel kapsamı kapsar.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "conditional_overgeneralization",
                "trap_title": "'However' Bağlacının Yanındaki Sıfatı Cümle Sonunda Bırakmak",
                "explanation_tr": "'*However this change looks minor' dizimi yanlıştır; 'however' anlamca 'ne kadar' demekse hemen arkasına sıfatı almalıdır: 'However minor this change looks'.",
                "incorrect_example": "However the deadline seems tight, never skip determinism tests.",
                "correct_example": "However tight the deadline seems, never skip determinism tests.",
            }
        ],
        "examples": [
            {
                "en": "However subtle an accent variation may sound, semantic intelligibility matters first.",
                "tr": "Bir aksan farklılığı ne kadar ince duyulursa duyulsun, önce anlamsal anlaşılırlık önem taşır.",
                "rule_highlight": "However subtle ... may sound",
                "context": "coaching principle",
            },
            {
                "en": "Whichever region leads pilot adoption will validate offline audio packs first.",
                "tr": "Pilot benimsemeye hangi bölge öncülük ederse etsin önce çevrimdışı ses paketlerini doğrulayacaktır.",
                "rule_highlight": "Whichever region leads",
                "context": "rollout plan",
            },
        ],
        "topic_tags": ["conditionals", "policy", "writing"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c1.prepositional-complement-variation",
        "title": "Executive Prepositional Government: 'Stem From', 'Account For', 'Comply With'",
        "cefr_level": "C1",
        "category": "prepositions_and_particles",
        "summary_en": "Lock formal business and analytical verb-preposition frames—'comply with', 'stem from', 'account for', 'refrain from', 'attribute X to Y'—without mixing semantic neighbors.",
        "summary_tr": "Analitik ve kurumsal fiillerin zorunlu edat eşleşmelerini ('stem from, comply with, attribute X to Y, hinge on') yakın anlamlı komşularının edatlarıyla karıştırmadan kurun.",
        "explanation_en": [
            {
                "title": "Preventing Synonym Preposition Drift",
                "content": "Learners often cross-wire synonyms: mixing 'obey' (no preposition) with 'comply with', 'depend on' with 'hinge upon/on', or 'cause by' with 'stem from'. Notice too 'attribute effect A to cause B' versus 'result from cause B'.",
                "patterns": [
                    "comply with [rule] / adhere to [standard] / abide by [terms]",
                    "stem from [root cause] | attribute [effect] to [source]",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'kurala uymak' tek '-a' ekiyle kurulurken İngilizcede 'comply WITH', 'adhere TO', 'obey (edatsız)' şeklinde her fiil kendi sabit edatına bağlıdır.",
        "rules": [
            {
                "name": "Cause vs. Compliance Frame Accuracy",
                "pattern": "Effect stems from Cause | Analyst attributes Effect to Cause",
                "use_cases": ["Describing root-cause analyses, regulatory audits, and KPI drivers"],
                "time_markers": ["in Q3", "across logs"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "The gain stems from better caching.",
                "structure_b": "Engineering attributed the gain to better caching.",
                "difference_explanation_en": "'Stem from' puts the effect in active subject position; 'attribute X to Y' credits a cause explicitly.",
                "difference_explanation_tr": "'Stem from' sonucun kaynağını doğrudan anlatır; 'attribute X to Y' etken özneyle ilişkilendirir.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "preposition_trap",
                "trap_title": "'Comply To' veya 'Result By' Gibi Çapraz Edat Eşleşmeleri",
                "explanation_tr": "'Adhere to' yüzünden '*comply to' yazmak veya 'caused by' yüzünden '*stem by' demek C1 yazılarında sıkça düzeltilen edat kaymalarıdır.",
                "incorrect_example": "All subcontractors must comply to security rules that stem by recent audits.",
                "correct_example": "All subcontractors must comply with security rules that stem from recent audits.",
            }
        ],
        "examples": [
            {
                "en": "Nearly eighty percent of support tickets stemmed from two confusing onboarding steps.",
                "tr": "Destek taleplerinin neredeyse yüzde sekseni iki kafa karıştırıcı ilk kullanım adımından kaynaklandı.",
                "rule_highlight": "stemmed from",
                "context": "root-cause memo",
            },
            {
                "en": "Long-term enterprise trust hinges on predictable security responses rather than slogans.",
                "tr": "Uzun vadeli kurumsal güven sloganlardan ziyade öngörülebilir güvenlik yanıtlarına dayanır.",
                "rule_highlight": "hinges on",
                "context": "leadership strategy",
            },
        ],
        "topic_tags": ["prepositions", "analysis", "governance"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    # ==================== C2 (12 lessons) ====================
    {
        "id": "grammar.c2.negative-polarity-items-subtleties",
        "title": "Negative Polarity Licensing: 'Let Alone', 'Much Less', 'Remotely', 'At All'",
        "cefr_level": "C2",
        "category": "inversion_and_emphasis",
        "summary_en": "Deploy negative polarity items ('let alone', 'much less', 'anything remotely', 'in the least') accurately inside negative, interrogative, or implicit scale-downward environments.",
        "summary_tr": "'Let alone' (şöyle dursun / bir yana), 'much less' ve 'remotely / in the least' gibi olumsuzluk kutbu ifadelerini yalnızca olumsuz, soru veya kısıtlayıcı bağlamlarda doğru sırayla kullanın.",
        "explanation_en": [
            {
                "title": "Downward-Entailing Triggers for Scalar Emphasis",
                "content": "Expressions like 'let alone' and 'much less' require an explicit negative or semi-negative premise ('seldom', 'hardly', 'reluctant to', 'without') ranking an easier baseline first before ruling out an extreme second target: 'It fails to clear staging, let alone production.'",
                "patterns": [
                    "Negative Premise (unlikely/hardly/fail to X), + let alone / much less + Greater Y",
                    "not / hardly / without + anything remotely + Adjective",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Şunu bile yapamıyor, kaldı ki bunu yapsın' yapısı 'let alone' ile kurulur; ancak ilk cümlecik olumluysa '*He agrees to sign, let alone review' anlamsız olur. Ayrıca ikinci unsur daima birinciden daha zor veya kapsamlı olmalıdır.",
        "rules": [
            {
                "name": "Direction of Effort in 'Let Alone'",
                "pattern": "Cannot achieve Easy Baseline X, let alone Demanding Target Y",
                "use_cases": ["Articulating sharp analytical boundaries and feasibility limits"],
                "time_markers": ["scarcely", "never", "without"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "The parser cannot handle basic hyphens, let alone nested Turkish suffixes.",
                "structure_b": "The draft lacked anything remotely approaching consensus.",
                "difference_explanation_en": "'Let alone' links scaled alternatives under negation; 'anything remotely' intensifies an absent standard.",
                "difference_explanation_tr": "'Let alone' olumsuzda kademeli karşılaştırma kurar; 'anything remotely' yokluğu vurgular.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "'Let Alone' Öncesinde Olumlu Yüklem Kurmak veya Ölçeği Ters Çevirmek",
                "explanation_tr": "Daha zor olanı başa, kolayı sona koymak ('*cannot run a marathon, let alone walk') veya pozitif cümlede 'let alone' kullanmak C2 düzeyinde mantık kırılması yaratır.",
                "incorrect_example": "The pipeline can verify full APK hashes, let alone single strings.",
                "correct_example": "The pipeline struggles to reconcile basic strings, let alone full SQLite hashes.",
            }
        ],
        "examples": [
            {
                "en": "Without audit timestamps, external counsel cannot verify local edits, let alone certify compliance.",
                "tr": "Denetim zaman damgaları olmadan dış hukuk danışmanları yerel değişiklikleri doğrulayamaz, uyumluluğu onaylamak bir yana.",
                "rule_highlight": "cannot verify local edits, let alone certify compliance",
                "context": "legal risk brief",
            },
            {
                "en": "Regional directors expressed nothing remotely resembling enthusiasm for mandatory overtime.",
                "tr": "Bölge direktörleri zorunlu fazla mesai için uzaktan yakından heyecana benzeyen hiçbir tutum sergilemedi.",
                "rule_highlight": "nothing remotely resembling",
                "context": "executive summary",
            },
        ],
        "topic_tags": ["negation", "rhetoric", "writing"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c2.hedged-attribution-evidentials",
        "title": "Evidential & Source-Distancing Frames: 'By All Accounts', 'Ostensibly', 'Purport To'",
        "cefr_level": "C2",
        "category": "modals_and_semi_modals",
        "summary_en": "Calibrate epistemic responsibility in intelligence briefs using evidential frames ('by all accounts', 'to all appearances', 'ostensibly', 'purportedly') that separate verified facts from reported claims.",
        "summary_tr": "Strateji ve denetim metinlerinde doğrulanmış olgu ile karşı tarafın iddiasını ya da dış gözlemi ayırmak için 'ostensibly', 'purportedly', 'by all accounts' gibi kanıt çerçevelerini kullanın.",
        "explanation_en": [
            {
                "title": "Precision Calibration of Evidential Weight",
                "content": "Calling something 'ostensibly simple' signals surface simplicity masking likely hidden complexity. 'Purport to' distances the speaker from an unverified vendor claim. 'By all accounts' aggregates multiple consistent witnesses while signaling you rely on secondary testimony.",
                "patterns": [
                    "ostensibly + Adj/Purpose (= on the surface, though perhaps masking another motive)",
                    "purport + to + Verb | By all accounts, + Clause",
                ],
            }
        ],
        "explanation_tr": "Türkçede '-miş gibi / görünüşte / söylenene göre' ekleri tek kipte toplanırken İngilizcede kaynak güvenirlik tonunu kesin kelimeler taşır: 'ostensibly' yüzeydeki görünüşten şüphe uyandırır, 'presumably' makul tahmin yürütür.",
        "rules": [
            {
                "name": "Matching Source Stance to Evidence",
                "pattern": "Verified report = by all accounts | Suspect facade = ostensibly | Unproven claim = purportedly",
                "use_cases": ["Drafting vendor due-diligence memos without asserting unverified rumors as fact"],
                "time_markers": ["historically", "pending inspection"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "The surcharge was ostensibly introduced to cover courier insurance.",
                "structure_b": "By all accounts, the Q3 transition ran far smoother than expected.",
                "difference_explanation_en": "'Ostensibly' implies a possible hidden motive; 'by all accounts' endorses broad consensus.",
                "difference_explanation_tr": "'Ostensibly' görünürdeki bahaneye kuşku katar; 'by all accounts' ise ortak gözlemi aktarır.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "aspect_confusion",
                "trap_title": "Tarafsız 'Apparently' Yerine Şüphe İma Eden 'Ostensibly / Purportedly' Seçmek",
                "explanation_tr": "Övdüğünüz kendi ekibiniz için 'purportedly delivered cleanly' yazmak sanki doğruluğundan kuşku duyuyormuşsunuz gibi alaycı tını verir.",
                "incorrect_example": "Our internal QA team purportedly verified every Turkish trap note accurately.",
                "correct_example": "By all accounts, our editorial QA team verified every Turkish trap note thoroughly.",
            }
        ],
        "examples": [
            {
                "en": "The script purported to deduplicate vocabulary automatically, yet skipped plural variants.",
                "tr": "Betik kelimeleri otomatik tekilleştirdiğini iddia ediyordu ancak çoğul biçimleri atlıyordu.",
                "rule_highlight": "purported to deduplicate",
                "context": "audit analysis",
            },
            {
                "en": "Ostensibly a minor UI refresh, the update silently changed how notification opt-ins stored defaults.",
                "tr": "Görünüşte ufak bir arayüz yenilemesi olan güncelleme bildirim izinlerinin varsayılanlarını sessizce değiştirdi.",
                "rule_highlight": "Ostensibly a minor UI refresh",
                "context": "privacy assessment",
            },
        ],
        "topic_tags": ["hedging", "diplomacy", "analysis"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c2.focus-particles-even-only-placement",
        "title": "Focus Particle Precision: Positioning 'Only', 'Merely', 'Solely', and 'Even'",
        "cefr_level": "C2",
        "category": "cleft_sentences",
        "summary_en": "Prevent costly contractual ambiguities by placing focusing adverbs ('only', 'solely', 'even', 'primarily') directly before the exact constituent they restrict.",
        "summary_tr": "'Only', 'solely', 'merely' ve 'even' odak zarflarını fiil önüne rastgele atmak yerine tam olarak kısıtladıkları kelime öbeğinin hemen önüne yerleştirerek sözleşme belirsizliğini önleyin.",
        "explanation_en": [
            {
                "title": "Why Clause Position Shifts Truth Conditions",
                "content": "Speech tolerates 'We only guarantee uptime locally' by stressing 'locally' vocally. Formal writing lacks voice stress: write 'We guarantee uptime solely for offline storage' vs. 'Only our platform guarantees local uptime' vs. 'We can merely verify offline tables.'",
                "patterns": [
                    "solely / exclusively + Prepositional Phrase (for / during / within...)",
                    "Only + [Restricted NP] + Verb (subject restriction without inversion? Compare vs. 'Only after X + Aux + Subj')",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'sadece / bile' odaklayacağı kelimenin hemen önüne veya arkasına vurguyla oturur. İngilizce resmi sözleşmelerde 'We only billed active seats' ile 'We billed only active seats' farklı yorum davalarına yol açabilir.",
        "rules": [
            {
                "name": "Adjacent Attachment of Focus Adverbs",
                "pattern": "Restrictive Particle (only / solely / purely) + Immediately Preceded Target Constituent",
                "use_cases": ["Writing airtight SLA scope clauses and technical specifications"],
                "time_markers": ["solely", "exclusively", "merely"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Audit logs capture only failed sign-in attempts.",
                "structure_b": "Only audit logs capture failed sign-in attempts.",
                "difference_explanation_en": "A limits WHICH events get logged; B limits WHICH artifact holds them.",
                "difference_explanation_tr": "A hangi olayların kaydedildiğini, B ise bu kaydı hangi dosyanın tuttuğunu sınırlar.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "'Only' Zarfını Fiilin Önüne Atıp Kısıtlanan Öbeği Belirsiz Bırakmak",
                "explanation_tr": "'Sadece 50 kullanıcıyı kapsar' anlamında '*It only covers 50 users for local storage' yazmak hem sayıyı hem depolamayı muğlaklaştırır; daraltılan öğenin başına getirin.",
                "incorrect_example": "The warranty only remains valid if buyers keep invoices for six months.",
                "correct_example": "The warranty remains valid only if buyers retain original invoices for six months.",
            }
        ],
        "examples": [
            {
                "en": "Runtime AI triggers solely during live speaking sessions, leaving reading flows completely offline.",
                "tr": "Çalışma zamanı yapay zekası yalnızca canlı konuşma oturumlarında devreye girer ve okuma akışlarını tamamen çevrimdışı bırakır.",
                "rule_highlight": "triggers solely during",
                "context": "architecture spec",
            },
            {
                "en": "Adding one unvalidated symbol could break not merely imports, but even packaged Room reads.",
                "tr": "Doğrulanmamış tek bir sembol eklemek yalnızca aktarımları değil, paketlenen Room okumalarını bile bozabilir.",
                "rule_highlight": "not merely ... but even",
                "context": "validation rule",
            },
        ],
        "topic_tags": ["precision", "legal", "writing"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c2.resumptive-modifiers-appositive-dash",
        "title": "Resumptive & Summative Modifiers: Synthesizing Long Clauses with Em-Dash Appositives",
        "cefr_level": "C2",
        "category": "relative_and_participle_clauses",
        "summary_en": "Replace sprawling 'which'-clauses after long premises with a crisp resumptive noun or em-dash summative modifier ('—a safeguard that...', '—a dynamic largely ignored').",
        "summary_tr": "Uzun ön cümlelerin sonunda belirsiz bir 'which' bırakmak yerine, tüm durumu tek isimle özetleyen açımlamalı bir apozitif ('—a step that...', '—an oversight that...') kurun.",
        "explanation_en": [
            {
                "title": "Fixing Vague Broad-Reference 'Which' Clauses",
                "content": "Ending a complex paragraph with '...which raised compliance concerns' leaves readers guessing which phrase 'which' refers to. C2 writers bridge smoothly using a summarizing noun (a step, a safeguard, a trend, a concession) repeated as an appositive or attached to 'which/that'.",
                "patterns": [
                    "[Complex Proposition]—a [Summarizing Noun] that + Verb...",
                    "[Parallel NPs]—practices that collectively + Verb...",
                ],
            }
        ],
        "explanation_tr": "Türkçede '...ki bu durum...' oldukça akıcıdır. İngilizcede uzun cümle sonuna çıplak ', which...' eklemek göndermeyi bulandırır; araya özetleyici bir isim ('—a shift that') koymak metne editoryal ustalık katar.",
        "rules": [
            {
                "name": "Summative Noun Encapsulation",
                "pattern": "Clause, a [category noun: discipline/trend/bottleneck] that + Predicate",
                "use_cases": ["Crafting high-impact executive conclusions and academic syntheses"],
                "time_markers": ["ultimately", "consequently"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "The build hashes source files cleanly, which satisfies auditors.",
                "structure_b": "The build hashes source files cleanly—a discipline that satisfies external auditors.",
                "difference_explanation_en": "B avoids pronoun ambiguity by labeling the prior entire action explicitly as 'a discipline'.",
                "difference_explanation_tr": "B 'which' zamirini açıkta bırakmayıp önceki süreci 'a discipline' diye adlandırarak netleştirir.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "Uzun Cümle Sonunda Belirsiz 'Which' Bırakıp Özneyi Kaybetmek",
                "explanation_tr": "Bir paragrafın sonuna tek başına ', which helped sales' eklemek son kelimeye mi yoksa tüm cümleye mi bağlandığını belirsiz kılar.",
                "incorrect_example": "Regional reps shortened SLA redlines and pre-filled tax data, which won fast sign-off.",
                "correct_example": "Regional reps shortened SLA redlines and pre-filled tax data—streamlining steps that won fast sign-off.",
            }
        ],
        "examples": [
            {
                "en": "Every batch passes pre-build schema gates—a discipline that eliminates broken seeds offline.",
                "tr": "Her paket derleme öncesi şema kapılarından geçer; bu da çevrimdışı bozuk verileri baştan ortadan kaldıran bir disiplindir.",
                "rule_highlight": "—a discipline that eliminates",
                "context": "engineering charter",
            },
            {
                "en": "Both parties agreed on thirty-day cure windows, a concession without which talks would have stalled.",
                "tr": "İki taraf da otuz günlük düzeltme sürelerinde anlaştı; bu olmasa görüşmelerin kilitleneceği bir tavizdi.",
                "rule_highlight": "a concession without which",
                "context": "negotiation recap",
            },
        ],
        "topic_tags": ["writing", "cohesion", "executive"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c2.archaic-legal-compound-adverbs",
        "title": "Contractual Compound Adverbs ('Thereby', 'Thereof', 'Wherein', 'Hereto'): Decode & Reformulate",
        "cefr_level": "C2",
        "category": "prepositions_and_particles",
        "summary_en": "Decode legal pronominal compounds ('thereby = by that means', 'thereof = of that', 'wherein = in which') accurately and know when plain-English equivalents outperform stiffness.",
        "summary_tr": "Sözleşme metinlerindeki 'thereby' (bunun vasıtasıyla), 'thereof' (bunun), 'wherein' (içinde/kapsamında) ve 'hereto' gibi birleşik zarfları doğru çözün ve gerektiğinde sade iş diline aktarın.",
        "explanation_en": [
            {
                "title": "Internal Anatomy of Formal Pronominal Compounds",
                "content": "'Here-' = 'this document'; 'There-' = 'that prior item'; 'Where-' = 'which'. So 'thereby' = 'by doing that', 'thereafter' = 'after that time', 'hereunder' = 'under this agreement'. Keep 'thereby + V-ing' active in executive summaries ('thereby cutting lag'), while simplifying clunky 'thereto/heretofore' in normal emails.",
                "patterns": [
                    "Clause, thereby + V-ing (still crisp and idiomatic in executive prose)",
                    "thereof (= of it) / hereunder (= under this contract) — passive reading mastery",
                ],
            }
        ],
        "explanation_tr": "Türkçe şirket avukatları ve üst düzey yöneticiler yabancı sözleşmelerde 'the parties hereto' (bu sözleşmenin tarafları) veya 'portion thereof' (bunun kısmı) kalıplarıyla sık karşılaşır; ancak bunları gündelik e-postalara taşımak kaba ve aşırı yapay kaçar.",
        "rules": [
            {
                "name": "'Thereby + -ing' for Causal Efficiency",
                "pattern": "Action Clause + , + thereby + Participle (-ing) + Outcome",
                "use_cases": ["Reading MSAs accurately and summarizing operational benefits concisely"],
                "time_markers": ["thereafter", "thereby", "hereunder"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "The validator hashes outputs cleanly, thereby guaranteeing deterministic builds.",
                "structure_b": "Please attach the updated price schedule hereto and return it post-haste.",
                "difference_explanation_en": "A ('thereby guaranteeing') sounds sharp in modern tech writing; B ('hereto / post-haste') sounds stiff outside legal deeds.",
                "difference_explanation_tr": "A modern teknik yazıda doğal ve keskindir; B ise sıradan e-postada aşırı resmi/ağdalı durur.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "preposition_trap",
                "trap_title": "'Thereby' Yerine 'Therefore' Kullanıp Fiili '-ing' Bırakmak",
                "explanation_tr": "'Böylece maliyetleri düşürdü' anlamında '*cuts lag, therefore saving time' veya '*thereby saves' hatalarına dikkat edin: 'therefore' sonuç bağlacıdır (tam yüklem ister), 'thereby' ise 'bu yolla' araç zarfıdır (-ing alır).",
                "incorrect_example": "Local storage avoids live calls, therefore reducing monthly inference expenses.",
                "correct_example": "Local storage avoids live calls, thereby reducing monthly inference expenses.",
            }
        ],
        "examples": [
            {
                "en": "Indexing topic relations upfront simplifies adaptive sorting, thereby saving battery offline.",
                "tr": "Konu ilişkilerini baştan dizinlemek uyarlanabilir sıralamayı basitleştirir ve bu yolla çevrimdışı pilden tasarruf sağlar.",
                "rule_highlight": "thereby saving",
                "context": "architecture design doc",
            },
            {
                "en": "All exhibits attached hereto form an enforceable component of the master services agreement.",
                "tr": "İşbu sözleşmeye eklenen tüm ekler ana hizmet sözleşmesinin bağlayıcı bir parçasını oluşturur.",
                "rule_highlight": "attached hereto",
                "context": "legal agreement",
            },
        ],
        "topic_tags": ["legal", "register", "writing"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c2.pragmatic-concession-mind-you-then-again",
        "title": "Conversational Concession & Calibration: 'Granted', 'Admittedly', 'Mind You', 'Then Again'",
        "cefr_level": "C2",
        "category": "discourse_markers_and_cohesion",
        "summary_en": "Modulate spoken and interactive debates naturally with nuanced pragmatic concessives ('admittedly', 'granted', 'mind you', 'having said that', 'then again') instead of overusing 'but'.",
        "summary_tr": "Sözlü tartışmalarda ve yönetici görüşmelerinde her itirazı basitçe 'but' ile kesmek yerine 'granted / admittedly' (kabul ediyorum ki), 'mind you / having said that' ve 'then again' ile ince ayar yapın.",
        "explanation_en": [
            {
                "title": "Three-Move Pragmatic Balancing in Real Time",
                "content": "Master conversationalists signal open-mindedness in three crisp beats: acknowledge a limitation ('Granted, initial packaging adds 15 seconds'), pivot tactfully ('mind you / that said'), and counter-weigh ('even offline, cold launch drops below one second').",
                "patterns": [
                    "Granted / Admittedly, [Concession]. Mind you / Even so, [Counterbalance].",
                    "[Point A]. Then again, [Alternative perspective softening certainty].",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'gerçi', 'bununla birlikte', 'diğer yandan bakınca' geçişleri sohbet akışını yumuşatır. C2 seviyesinde konuşan biri sürekli 'but / however' demek yerine bağlama göre 'mind you' (yine de şunu unutma ki) veya 'then again' (ama öte yandan) seçer.",
        "rules": [
            {
                "name": "Concession Before Counter-Point",
                "pattern": "Granted/Admittedly + True Caveat + ; + that said / still / yet + Main Takeaway",
                "use_cases": ["Handling skeptical executive questions cleanly in live Q&A sessions"],
                "time_markers": ["granted", "that said", "then again"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Admittedly, initial batch generation takes ten seconds.",
                "structure_b": "Wait—then again, maybe enterprise legal teams prefer manual CSV exports.",
                "difference_explanation_en": "'Admittedly' concedes a clear shortcoming openly; 'then again' weighs an unexpected opposite angle.",
                "difference_explanation_tr": "'Admittedly' bilinen bir kısıtı açıkça kabul eder; 'then again' ise diğer ihtimali masaya koyar.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "conditional_overgeneralization",
                "trap_title": "Sözlü Kalıp 'Mind You'yu 'Sizi Dikkat Ediyorum / Önemsiyorum' Sanmak",
                "explanation_tr": "'Mind you' kelimesi kelimesine 'seni umursamak' değildir; cümle başında/sonunda 'ama şunu da eklemek gerekir ki (gerçi)' anlamlı bir konuşma belirtecidir.",
                "incorrect_example": "We accept the budget; mind you your hard work on the deck.",
                "correct_example": "The tier looks affordable—mind you, onboarding support fees sit on a separate schedule.",
            }
        ],
        "examples": [
            {
                "en": "Granted, maintaining 150 grammar lessons takes discipline; still, learner retention proves the value.",
                "tr": "Kabul etmek gerekir ki 150 dilbilgisi dersini sürdürmek disiplin istiyor; yine de öğrenci tutma oranları değerini kanıtlıyor.",
                "rule_highlight": "Granted, ... still,",
                "context": "panel discussion",
            },
            {
                "en": "The margin looked thin at first—then again, support automation cut recurring labor significantly.",
                "tr": "Başta kâr marjı dar görünüyordu; ama öte yandan destek otomasyonu düzenli iş gücünü ciddi biçimde azalttı.",
                "rule_highlight": "then again",
                "context": "leadership dialogue",
            },
        ],
        "topic_tags": ["speaking", "diplomacy", "discourse_markers"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c2.future-past-unfulfilled-perfect",
        "title": "Unfulfilled Past Expectations: 'Was to Have Signed', 'Would Have Expected'",
        "cefr_level": "C2",
        "category": "tenses_and_aspect",
        "summary_en": "Distinguish open-outcome planned events ('was to launch' — may have happened) from explicitly aborted historical intentions ('was to have launched' — definitely never happened).",
        "summary_tr": "Gerçekleşip gerçekleşmediği bağlama açık olan 'was to + V1' kalıbıyla, kesinlikle iptal edildiği/gerçekleşmediği vurgulanan 'was to have + V3' arasındaki farkı kullanın.",
        "explanation_en": [
            {
                "title": "Foreclosed History in Official Post-Mortems",
                "content": "Both 'was to begin' and 'was to have begun' talk about past schedules. Crucially, 'The audit was to begin Tuesday' can continue cleanly with '...and finished on time', whereas 'The audit was to have begun Tuesday' immediately alerts readers that the Tuesday start fell through.",
                "patterns": [
                    "was/were + to + V1 (neutral past destiny or arrangement)",
                    "was/were + to have + V3 (aborted arrangement that never materialized)",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Salı başlaması planlanıyordu' cümlesi başlasa da başlamasa da benzer kurulur; ancak İngilizcede 'was to have started' dinleyiciye ilk kelimeden itibaren o planın iptal olduğu sinyalini verir.",
        "rules": [
            {
                "name": "Perfect Infinitive After Past 'Be To'",
                "pattern": "Subject + was/were + to have + V3 (+ before disruption X)",
                "use_cases": ["Summarizing canceled M&A closings, postponed launches, and disrupted timelines"],
                "time_markers": ["originally", "last spring"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Phase 8 was to shape our long-term offline architecture, and so it did.",
                "structure_b": "The courier was to have arrived Tuesday, yet customs strikes halted transit.",
                "difference_explanation_en": "'Was to shape' reports fulfilled outcome; 'was to have arrived' marks an unfulfilled plan.",
                "difference_explanation_tr": "'Was to' gerçekleşen tarihi sonucu; 'was to have V3' ise boşa çıkan planı vurgular.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "aspect_confusion",
                "trap_title": "Gerçekleşmiş Başarıyı Anlatırken Yanlışlıkla 'Was To Have V3' Kurmak",
                "explanation_tr": "Başarıyla tamamlanan bir lansmanı '*The rollout was to have happened on time and succeeded' diye anlatmak mantık çelişkisi yaratır.",
                "incorrect_example": "The database build was to have passed cleanly on Monday, which delighted clients.",
                "correct_example": "The server migration was to have closed Friday, yet hardware delays pushed it to Monday.",
            }
        ],
        "examples": [
            {
                "en": "The joint summit was to have convened in Ankara until severe winter storms grounded flights.",
                "tr": "Ortak zirve sert kış fırtınaları uçuşları durdurana kadar Ankara'da toplanacaktı (toplanamadı).",
                "rule_highlight": "was to have convened",
                "context": "executive briefing",
            },
            {
                "en": "That early prototype, modest at launch, was to become the backbone of the offline suite.",
                "tr": "Başlangıçta mütevazı olan o erken prototip, ileride çevrimdışı paketin omurgası haline gelecekti.",
                "rule_highlight": "was to become",
                "context": "retrospective narrative",
            },
        ],
        "topic_tags": ["tenses", "writing", "reporting"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c2.tough-movement-evaluative-adjectives",
        "title": "Tough-Movement & Evaluative Infinitives: 'Easy to Audit' Without Trailing 'It'",
        "cefr_level": "C2",
        "category": "verb_patterns_and_infinitives",
        "summary_en": "Promote an infinitive's object to sentence subject cleanly ('This SLA is hard to defend' — omitting trailing 'it'), and avoid impossible subjects with 'possible/impossible/probable'.",
        "summary_tr": "'This bug is hard to trace' gibi yapılarda nesne zaten özne konumuna taşındığı için sona fazladan 'it' koymayın; ayrıca 'possible/probable' sıfatlarını kişisel özneye bağlamayın.",
        "explanation_en": [
            {
                "title": "Syntactic Constraints on Evaluative Adjectives",
                "content": "Evaluative adjectives like 'easy, hard, tricky, impossible' undergo tough-movement ('This rule is tricky to enforce' — note zero 'it' after 'enforce'!). However, keep 'for' + agent clean rather than '*for fixing', and remember '*The timeline is probable to slip' is invalid—use 'It is likely that...' or 'is likely to slip'.",
                "patterns": [
                    "Object-Turned-Subject + be + easy/hard/tough + to + Transitive Verb (NO trailing 'it'!)",
                    "It is impossible + to V1 (OK) vs. Subject is impossible + to V1 (rare/restricted -> avoid for inanimate probability)",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Bu hatayı yakalamak zor' derken zamir tekrarı yapılabilir; İngilizcede ise '*This error is hard to catch it' hatası çok sıktır. Ayrıca '*He is impossible to come' yerine 'It is impossible for him to come' gerekir.",
        "rules": [
            {
                "name": "Zero Pronoun Trace in Tough-Movement",
                "pattern": "Asset X + is + easy/difficult/complex + to + Verb (+ Preposition) [NO object pronoun!]",
                "use_cases": ["Evaluating usability, legal enforceability, and technical maintainability smoothly"],
                "time_markers": ["under load", "in practice"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Legacy tax scripts are notoriously hard to debug.",
                "structure_b": "This proposal is likely to pass steering committee.",
                "difference_explanation_en": "'Hard' promotes the object ('debug [X]') leaving no trailing 'it'; 'likely' accepts normal subject raising.",
                "difference_explanation_tr": "'Hard' yapısında nesne özneye taşındığı için sonda 'it' kalmaz; 'likely' ise normal özneyi alır.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "voice_and_causative_transfer",
                "trap_title": "'Hard To Audit It' Fazla Zamiri ve '*For To Fix' Hatası",
                "explanation_tr": "'Anlaması zor' kalıbını '*difficult to understand it' ya da '*difficult for understanding' şeklinde kurmak C2 editörlük standartlarında hemen elenir.",
                "incorrect_example": "That multi-column invoice is quite tricky to reconcile it offline.",
                "correct_example": "That multi-column invoice is quite tricky to reconcile offline.",
            }
        ],
        "examples": [
            {
                "en": "Cleanly partitioned YAML files are remarkably straightforward to validate automatically.",
                "tr": "Temiz biçimde bölümlenmiş YAML dosyalarını otomatik doğrulamak son derece kolaydır.",
                "rule_highlight": "straightforward to validate",
                "context": "engineering docs",
            },
            {
                "en": "Negotiators warned that fixed multi-year caps would be tough to renegotiate mid-contract.",
                "tr": "Müzakereciler sabit çok yıllık üst sınırların sözleşme ortasında yeniden müzakere edilmesinin zor olacağı uyarısında bulundu.",
                "rule_highlight": "tough to renegotiate",
                "context": "commercial strategy",
            },
        ],
        "topic_tags": ["syntax", "infinitives", "writing"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c2.causal-precision-owing-due-for-fear",
        "title": "Causal & Precautionary Precision: 'Owing to', 'Due to', 'For Fear That', 'On Account of'",
        "cefr_level": "C2",
        "category": "discourse_markers_and_cohesion",
        "summary_en": "Separate strictly conservative noun-modifying 'due to' (after 'be' or noun) from adverbial 'owing to / because of', alongside formal precautionary 'for fear that / lest'.",
        "summary_tr": "Geleneksel resmi yazıda isim veya 'be' fiili niteleyen 'due to' ile yüklem zarfı olarak çalışan 'owing to / on account of' ve önlem bağlacı 'for fear that'ı hatasız ayırın.",
        "explanation_en": [
            {
                "title": "Formal Prescriptive Clarity vs. Everyday Interchangeability",
                "content": "While conversational English mixes 'due to' freely, legal and formal copyeditors still check the clean distinction: 'The delay was due to customs checks' (predicate noun complement after 'be'), versus 'Owing to customs checks, the delay persisted' (adverbial cause). Add 'for fear that / for fear of -ing' for cautious motivation.",
                "patterns": [
                    "be + due to + Noun Phrase | Noun + due to + Noun Phrase",
                    "Owing to + Noun Phrase, + Full Main Clause",
                    "for fear that + Subject + (might/should) + Verb | for fear of + V-ing",
                ],
            }
        ],
        "explanation_tr": "Türkçede '...yüzünden / ...nedeniyle' tek yapıda karşılanır. Üst düzey İngilizce belgelerde 'due to'nun 'because of' yerine cümle başında kullanılmasına karşı resmi stil kılavuzları 'owing to'yu veya 'caused by'ı daha sağlam görür.",
        "rules": [
            {
                "name": "Conservative Predicate Test for 'Due To'",
                "pattern": "Can replace with 'caused by' -> due to | Can replace with 'because of' modifying verb -> owing to",
                "use_cases": ["Drafting incident root-cause notices and formal legal explanations cleanly"],
                "time_markers": ["owing to", "for fear of"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Our confidence loss was strictly due to unverified telemetry.",
                "structure_b": "Owing to unverified telemetry, we suspended public scoring.",
                "difference_explanation_en": "A follows linking verb 'was'; B modifies the main action verb 'suspended' cleanly.",
                "difference_explanation_tr": "A 'was' bağ fiilinin yüklemidir; B ise 'suspended' eyleminin gerekçesini anlatır.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "preposition_trap",
                "trap_title": "'Due To + Tam Cümle' Kullanmak veya 'Because Of' İle Fiili Yan Cümle Başlatmak",
                "explanation_tr": "'*Due to we ran out of storage' hatalıdır; 'due to / owing to / on account of' mutlaka isim öbeği ister, tam cümle değil (tam cümle için 'inasmuch as' veya 'due to the fact that' kullanılır).",
                "incorrect_example": "Owing to compliance requested revisions, launch shifted two days.",
                "correct_example": "Owing to compliance revision requests, launch shifted two days.",
            }
        ],
        "examples": [
            {
                "en": "Owing to strict determinism checks, identical inputs always yield byte-identical SQLite files.",
                "tr": "Sıkı determinizm kontrolleri sayesinde özdeş girdiler her zaman bayt düzeyinde aynı SQLite dosyalarını üretir.",
                "rule_highlight": "Owing to strict determinism checks",
                "context": "technical standard",
            },
            {
                "en": "Teams froze optional features early for fear that regression fixes might miss store deadlines.",
                "tr": "Ekipler gerileme düzeltmelerinin mağaza teslim tarihini kaçırma ihtimaline karşın isteğe bağlı özellikleri erkenden dondurdu.",
                "rule_highlight": "for fear that ... might miss",
                "context": "release management",
            },
        ],
        "topic_tags": ["discourse_markers", "legal", "writing"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c2.uncountable-shiftable-nouns-c2",
        "title": "Abstract Countability Shifts: 'Work/Works', 'Damage/Damages', 'Scope/Scopes'",
        "cefr_level": "C2",
        "category": "noun_phrases_and_articles",
        "summary_en": "Master lexical meaning shifts when normally uncountable nouns appear in plural legal or technical registers ('damages' = compensation vs. 'damage' = harm; 'works' = creative opus/plant).",
        "summary_tr": "Normalde sayılamayan isimlerin çoğul kullanıldığında bambaşka teknik/hukuki anlamlara geçtiği yapıları ('damage' hasar / 'damages' tazminat; 'advice' vs 'pieces of counsel') ayırt edin.",
        "explanation_en": [
            {
                "title": "Plural Inflection Changes Semantic Category",
                "content": "Uncountable 'damage, research, feedback, equipment, compliance' never take '-s' for mere quantity ('*three researches'). Yet when C2 legal or engineering texts intentionally pluralize 'damages' (financial restitution), 'grounds' (legal basis), or 'works' (engineering infrastructure), the word shifts to a specialized legal/technical sense.",
                "patterns": [
                    "damage (harm, uncountable) vs. damages (monetary court compensation, always plural)",
                    "work (labor, uncountable) vs. works (public infrastructure / collected authored titles)",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'hasarlar / araştırmalar / ekipmanlar' çoğulu normaldir; bu yüzden '*many informations / *heavy damages to the server' hataları yaygındır. 'Damages' yalnızca mahkeme tazminatında çoğul kullanılır.",
        "rules": [
            {
                "name": "Zero Plural for Mass Abstract Concepts vs. Legal Plurals",
                "pattern": "feedback/research/advice (singular verb) | damages/grounds/proceeds (specialized plural)",
                "use_cases": ["Distinguishing physical harm from contractual indemnification in legal reviews"],
                "time_markers": ["under clause 8", "across studies"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "The hardware outage caused severe reputational damage.",
                "structure_b": "The vendor agreed to pay liquidated damages for missed uptime.",
                "difference_explanation_en": "A names physical/brand harm ('damage', uncountable); B names financial restitution ('damages').",
                "difference_explanation_tr": "A zarar/hasarı (sayılamaz 'damage'), B ise maddi tazminat cezasını ('damages') ifade eder.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "preposition_trap",
                "trap_title": "Fiziksel veya Veri Zararı İçin 'Damages' Çoğulu Yazmak",
                "explanation_tr": "'Büyük hasarlar verdik' çevirisiyle '*caused heavy damages to the table' yazmak anlamı komik biçimde 'tazminat ödettik'e yaklaştırır; doğrusu 'extensive damage'dır.",
                "incorrect_example": "Skipping migration checks caused multiple damages to local user tables.",
                "correct_example": "Skipping migration checks caused extensive damage to local user tables.",
            }
        ],
        "examples": [
            {
                "en": "Recent pedagogical research suggests spaced recall outweighs passive rereading twice over.",
                "tr": "Güncel pedagojik araştırmalar aralıklı hatırlamanın pasif tekrar okumanın iki katı etkili olduğunu gösteriyor.",
                "rule_highlight": "research suggests",
                "context": "academic summary",
            },
            {
                "en": "The supplier disputed statutory damages on the grounds that force majeure clauses applied.",
                "tr": "Tedarikçi mücbir sebep maddelerinin geçerli olduğu gerekçesiyle yasal tazminat tutarına itiraz etti.",
                "rule_highlight": "disputed statutory damages on the grounds that",
                "context": "arbitration brief",
            },
        ],
        "topic_tags": ["nouns", "legal", "academic"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c2.subjunctive-condition-assuming-provided-save",
        "title": "Contractual Stipulations & Exception Conditionals: 'Provided That', 'Save That', 'Barring'",
        "cefr_level": "C2",
        "category": "conditionals_and_hypotheticals",
        "summary_en": "Introduce strict prerequisite carve-outs and legal provisos cleanly with 'provided (that)', 'on condition that', 'barring + NP', and 'save that / except insofar as'.",
        "summary_tr": "Hukuki ve stratejik belgelerde katı ön şart veya istisna şerhi düşerken 'provided (that)' (...şartıyla), 'barring' (...hariç/olmadıkça) ve 'save that' kalıplarını şimdiki kip şart kuralıyla kurun.",
        "explanation_en": [
            {
                "title": "No 'Will' Inside Conditional Proviso Clauses",
                "content": "Just like 'if', formal conditional linkers ('provided that', 'on condition that', 'assuming arguments hold') forbid future 'will' inside the condition clause: write 'Provided telemetry clears cleanly on Friday, rollout proceeds Monday.' Use 'barring + Noun' as a sleek negative precondition ('Barring customs delays...').",
                "patterns": [
                    "Provided (that) / On condition that + Present Simple, + Main Future/Modal",
                    "Barring + Unforeseen Event Noun, + Projected Outcome",
                    "Main Clause, + save that + Limited Legal Exception Clause",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Gümrük aksamazsa / aksaklık olmazsa' ile 'Tek bir istisna dışında...' yapıları sözleşme dilinde 'barring' ve 'save that' ile çok zarif kısalır; ancak ön koşul cümlesine 'will' sokmamaya özen gösterin.",
        "rules": [
            {
                "name": "Present Tense Provisos Under 'Provided / Assuming'",
                "pattern": "Provided that + Present Simple (condition) + , + Future / Modal (result)",
                "use_cases": ["Drafting go/no-go launch criteria and commercial indemnity exclusions"],
                "time_markers": ["pending", "barring delays", "save that"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Provided build integrity passes Friday, migration runs Monday.",
                "structure_b": "Barring unexpected carrier downtime, sync completes in ten minutes.",
                "difference_explanation_en": "'Provided' states a required positive gate; 'barring' carves out a blocking negative exception.",
                "difference_explanation_tr": "'Provided' sağlanması şart olan olumlu eşiği; 'barring' ise bozabilecek olumsuz istisnayı dışlar.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "conditional_overgeneralization",
                "trap_title": "'Provided That' İçi Yan Cümlede 'Will' Kullanmak",
                "explanation_tr": "'Onaylanacağı şartıyla' ifadesini '*provided that it will pass' şeklinde kurmak temel şart cümlesi kuralını bozar; doğrusu 'provided that it passes'tır.",
                "incorrect_example": "The license renews cleanly provided that finance will remit wire payment Monday.",
                "correct_example": "The license renews cleanly provided that finance remits wire payment Monday.",
            }
        ],
        "examples": [
            {
                "en": "The two content builds are identical, save that release signatures follow regional keychains.",
                "tr": "Sürüm imzalarının bölgesel anahtarlıklara uyması dışında iki içerik derlemesi birebir aynıdır.",
                "rule_highlight": "save that",
                "context": "release documentation",
            },
            {
                "en": "Barring unforeseen payment gateway maintenance, quarterly invoices settle automatically.",
                "tr": "Öngörülmeyen ödeme ağ geçidi bakımı yaşanmadığı sürece üç aylık faturalar otomatik tahsil edilir.",
                "rule_highlight": "Barring unforeseen",
                "context": "operations SLA",
            },
        ],
        "topic_tags": ["conditionals", "legal", "governance"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.c2.double-comparative-qualification",
        "title": "Analytical Degree Nuance: 'All the More', 'Far From', 'None the Worse / Wiser'",
        "cefr_level": "C2",
        "category": "cleft_sentences",
        "summary_en": "Sharpen rhetorical weight and comparative qualification using fixed degree idioms: 'all the more + adj (for/because)', 'far from + V-ing/adj', and 'none the + comparative'.",
        "summary_tr": "Analitik karşılaştırmalara ustalık katmak için 'all the more impressive for...' (buna rağmen/bu yüzden bir kat daha etkileyici), 'far from + -ing' (hiç de ... değil aksine) ve 'none the wiser' kalıplarını kurun.",
        "explanation_en": [
            {
                "title": "Expressing Amplified Value Despite—or Because of—Constraints",
                "content": "'All the more' adds extra significance precisely because of a cited condition ('Offline speed is all the more critical given subway blackouts'). Note 'far from + gerund/adjective' sits smoothly at the front to refute expectations cleanly without negative 'not'.",
                "patterns": [
                    "is + all the more + Adj / Adv + for + Noun / because...",
                    "Far from + V-ing / Adjective, + Subject + Opposite Verb...",
                    "no(ne) the + Comparative (wiser / worse / less urgent)",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Sorunu çözmek şöyle dursun, daha da karmaşıklaştırdı' kalıbı 'Far from solving the issue, it complicated...' ile birebir ve kusursuz biçimde verilir. Başındaki fiil daima '-ing' alır!",
        "rules": [
            {
                "name": "Fronted 'Far From' Requires Gerund or Adjective",
                "pattern": "Far from + V-ing / Adj + , + Subject + Main Clause (requires explicit subject match)",
                "use_cases": ["Correcting flawed executive narratives cleanly in high-stakes meetings"],
                "time_markers": ["all the more so", "far from"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Far from slowing QA down, deterministic seeds cut triage in half.",
                "structure_b": "Deterministic speed makes local caching all the more compelling for frequent flyers.",
                "difference_explanation_en": "'Far from' flips an anticipated critique; 'all the more' heightens relevance given context.",
                "difference_explanation_tr": "'Far from' beklenen olumsuzluğu tamamen tersine çevirir; 'all the more' ise bir neden yüzünden değeri katlar.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "aspect_confusion",
                "trap_title": "'Far From' Sonrasında Yalın Fiil veya Çekimli Cümle Kurmak",
                "explanation_tr": "'Maliyeti düşürmek bir yana' ifadesinde 'far from' bir edat öbeğidir; arkasına '*far from to reduce' veya '*far from it reduces' değil 'far from reducing' gelmelidir.",
                "incorrect_example": "Far from solve legacy churn, rushed discounts widened Q3 losses.",
                "correct_example": "Far from solving legacy churn, rushed discounts widened Q3 losses.",
            }
        ],
        "examples": [
            {
                "en": "Far from diluting rigor, pairing concise definitions with Turkish traps accelerates recall.",
                "tr": "Titizliği azaltmak şöyle dursun, özlü tanımları Türkçe tuzaklarla eşleştirmek hatırlamayı hızlandırır.",
                "rule_highlight": "Far from diluting rigor",
                "context": "curriculum manifesto",
            },
            {
                "en": "Maintaining deterministic packaging is all the more vital once curriculum entries cross five thousand.",
                "tr": "Müfredat kayıtları beş bini aştığında deterministik paketlemeyi korumak bir kat daha hayati hale gelir.",
                "rule_highlight": "is all the more vital",
                "context": "architecture note",
            },
        ],
        "topic_tags": ["comparison", "rhetoric", "writing"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
]

